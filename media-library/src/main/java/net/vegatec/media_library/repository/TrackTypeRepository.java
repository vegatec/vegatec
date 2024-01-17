package net.vegatec.media_library.repository;

import net.vegatec.media_library.domain.TrackType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrackType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackTypeRepository extends JpaRepository<TrackType, Long> {}
