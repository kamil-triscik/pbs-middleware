package com.pbs.middleware.server.features.filetransfer.upload.utils;

import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileProvided;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFinished;
import com.pbs.middleware.server.features.filetransfer.upload.events.TemporaryFileDeleted;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadCancelled;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadInitialized;

public abstract class UploadVisitor implements Visitor<UploadEvent> {

    @Override
    public void visit(UploadEvent event) {
        if (event instanceof UploadInitialized) {
            this.uploadStarted((UploadInitialized) event);
        } else if (event instanceof FileUploaded) {
            this.fileUploaded((FileUploaded) event);
        } else if (event instanceof TemporaryFileDeleted) {
            this.fileRemoved((TemporaryFileDeleted) event);
        } else if (event instanceof FileUploadFailed) {
            this.fileUploadFailed((FileUploadFailed) event);
        } else if (event instanceof UploadFinished) {
            this.allFilesUploaded((UploadFinished) event);
        } else if (event instanceof UploadFailed) {
            this.uploadCompleteFailed((UploadFailed) event);
        } else if(event instanceof UploadCancelled) {
            this.uploadCancelled((UploadCancelled) event);
        } else if (event instanceof FileProvided) {
            this.fileProvided((FileProvided) event);
        }
    }

    protected void uploadStarted(UploadInitialized event) {

    }

    protected void fileProvided(FileProvided event) {

    }

    protected void fileUploaded(FileUploaded event) {

    }

    protected void fileUploadFailed(FileUploadFailed event) {

    }

    protected void fileRemoved(TemporaryFileDeleted event) {

    }

    protected void allFilesUploaded(UploadFinished event) {

    }

    protected void uploadCompleteFailed(UploadFailed event) {

    }

    protected void uploadCancelled(UploadCancelled event) {

    }

}
