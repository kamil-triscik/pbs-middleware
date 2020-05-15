package com.pbs.middleware.server.features.filetransfer.download.utils;

import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.server.features.filetransfer.download.events.AllFilesFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadCancelled;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadConfirmed;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadError;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadInitialized;
import com.pbs.middleware.server.features.filetransfer.download.events.EmailSent;
import com.pbs.middleware.server.features.filetransfer.download.events.EmailSentFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloadRequested;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import com.pbs.middleware.server.features.filetransfer.download.events.FileFetchingFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.FileRemoved;
import com.pbs.middleware.server.features.filetransfer.download.events.FilesListFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFinished;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingLaunched;
import com.pbs.middleware.server.features.filetransfer.download.events.RemoteCleanFailed;

public abstract class DownloadVisitor implements Visitor<DownloadEvent> {

    @Override
    public void visit(DownloadEvent event) {
        if (event instanceof DownloadInitialized) {
            this.apply((DownloadInitialized) event);
        } else if (event instanceof FileDownloadRequested) {
            this.apply((FileDownloadRequested) event);
        } else if (event instanceof FilesListFetched) {
            this.apply((FilesListFetched) event);
        } else if (event instanceof FileDownloaded) {
            this.apply((FileDownloaded) event);
        } else if (event instanceof FileFetchingFailed) {
            this.apply((FileFetchingFailed) event);
        } else if (event instanceof AllFilesFetched) {
            this.apply((AllFilesFetched) event);
        } else if (event instanceof DownloadConfirmed) {
            apply((DownloadConfirmed) event);
        } else if (event instanceof PostProcessingLaunched) {
            apply((PostProcessingLaunched) event);
        } else if (event instanceof PostProcessingFailed) {
            apply((PostProcessingFailed) event);
        } else if (event instanceof PostProcessingFinished) {
            apply((PostProcessingFinished) event);
        } else if (event instanceof PostProcessingFailed) {
            apply((PostProcessingFailed) event);
        } else if (event instanceof DownloadCancelled) {
            apply((DownloadCancelled) event);
        } else if (event instanceof DownloadFailed) {
            apply((DownloadFailed) event);
        } else if (event instanceof EmailSent) {//
            apply((EmailSent) event);
        } else if (event instanceof EmailSentFailed) {//
            apply((EmailSentFailed) event);
        } else if (event instanceof RemoteCleanFailed) {
            apply((RemoteCleanFailed) event);
        } else if (event instanceof DownloadError) {
            apply((DownloadError) event);
        } else if (event instanceof DownloadFailed) {
            apply((DownloadFailed) event);
        } else if (event instanceof FileRemoved) {
            apply((FileRemoved) event);
        }
    }

    protected void apply(DownloadInitialized event) { }

    protected void apply(FileDownloadRequested event) { }

    protected void apply(FilesListFetched event) { }

    protected void apply(FileDownloaded event) { }

    protected void apply(FileFetchingFailed event) { }

    protected void apply(AllFilesFetched event) { }

    protected void apply(DownloadConfirmed event) { }

    protected void apply(PostProcessingLaunched event) { }

    protected void apply(PostProcessingFinished event) { }

    protected void apply(PostProcessingFailed event) { }

    protected void apply(DownloadCancelled event) { }

    protected void apply(DownloadFailed event) { }

    protected void apply(EmailSent event) { }

    protected void apply(EmailSentFailed event) { }

    protected void apply(DownloadError event) { }

    protected void apply(RemoteCleanFailed event) { }

    protected void apply(FileRemoved event) { }

}
