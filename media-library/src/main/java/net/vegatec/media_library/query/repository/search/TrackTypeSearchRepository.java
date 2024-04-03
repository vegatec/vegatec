package net.vegatec.media_library.query.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.stream.Stream;
import net.vegatec.media_library.query.domain.TrackType;
import net.vegatec.media_library.query.repository.TrackTypeRepository;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link TrackType} entity.
 */
public interface TrackTypeSearchRepository extends ElasticsearchRepository<TrackType, Long>, TrackTypeSearchRepositoryInternal {}

interface TrackTypeSearchRepositoryInternal {
    Stream<TrackType> search(String query);

    Stream<TrackType> search(Query query);

    @Async
    void index(TrackType entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TrackTypeSearchRepositoryInternalImpl implements TrackTypeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TrackTypeRepository repository;

    TrackTypeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TrackTypeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<TrackType> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<TrackType> search(Query query) {
        return elasticsearchTemplate.search(query, TrackType.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(TrackType entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TrackType.class);
    }
}
