package net.vegatec.media_library.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import net.vegatec.media_library.domain.MusicRequest;
import net.vegatec.media_library.repository.MusicRequestRepository;
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
 * Spring Data Elasticsearch repository for the {@link MusicRequest} entity.
 */
public interface MusicRequestSearchRepository extends ElasticsearchRepository<MusicRequest, Long>, MusicRequestSearchRepositoryInternal {}

interface MusicRequestSearchRepositoryInternal {
    Page<MusicRequest> search(String query, Pageable pageable);

    Page<MusicRequest> search(Query query);

    @Async
    void index(MusicRequest entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MusicRequestSearchRepositoryInternalImpl implements MusicRequestSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MusicRequestRepository repository;

    MusicRequestSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MusicRequestRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MusicRequest> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MusicRequest> search(Query query) {
        SearchHits<MusicRequest> searchHits = elasticsearchTemplate.search(query, MusicRequest.class);
        List<MusicRequest> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MusicRequest entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MusicRequest.class);
    }
}
