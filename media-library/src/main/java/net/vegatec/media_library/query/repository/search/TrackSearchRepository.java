package net.vegatec.media_library.query.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.repository.TrackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Track} entity.
 */
public interface TrackSearchRepository extends ElasticsearchRepository<Track, Long>, TrackSearchRepositoryInternal {}

interface TrackSearchRepositoryInternal {
    Page<Track> search(String query, Pageable pageable);

    Page<Track> search(Query query);

    @Async
    void index(Track entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TrackSearchRepositoryInternalImpl implements TrackSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TrackRepository repository;

    TrackSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TrackRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Track> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Track> search(Query query) {
        SearchHits<Track> searchHits = elasticsearchTemplate.search(query, Track.class);
        List<Track> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Track entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Track.class);
    }
}
