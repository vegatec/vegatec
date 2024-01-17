package net.vegatec.media_library.repository;

import net.vegatec.media_library.domain.MusicRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MusicRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicRequestRepository extends JpaRepository<MusicRequest, Long>, JpaSpecificationExecutor<MusicRequest> {}
