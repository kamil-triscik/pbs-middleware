package com.pbs.middleware.server.features.filetransfer.download.status.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadStatusRepository extends CrudRepository<DownloadStatus, UUID> {

    @Query("SELECT us FROM DownloadStatus us WHERE us.notified = false AND us.startDate < (extract(epoch from now()) - :delay)")
    List<DownloadStatus> findAllNotNotified(@Param("delay") Integer delay);

    @Query("SELECT us FROM DownloadStatus us WHERE us.startDate < (extract(epoch from now()) - :retention)")
    List<DownloadStatus> findAllOld(@Param("retention") Integer retention);

    @Modifying
    @Query("update DownloadStatus us set us.notified = true where us.id IN :ids")
    void setNotified(@Param("ids") List<UUID> ids);

}
