package net.vegatec.media_library.query.web.rest.errors;

import static org.junit.jupiter.api.Assertions.*;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch._types.ErrorResponse;
import net.vegatec.media_library.query.web.rest.errors.ElasticsearchExceptionMapper;
import net.vegatec.media_library.query.web.rest.errors.QuerySyntaxException;
import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;

class ElasticsearchExceptionMapperTest {

    @Test
    void testMapException() {
        ErrorCause rootCause = new ErrorCause.Builder().reason("Failed to parse query [").build();
        ErrorResponse response = new ErrorResponse.Builder()
            .error(new ErrorCause.Builder().reason("").rootCause(rootCause).build())
            .status(400)
            .build();
        ElasticsearchException esException = new ElasticsearchException("", response);
        UncategorizedElasticsearchException cause = new UncategorizedElasticsearchException("", esException);
        assertInstanceOf(
            QuerySyntaxException.class,
            ElasticsearchExceptionMapper.mapException(new UncategorizedElasticsearchException("", cause))
        );
    }
}
