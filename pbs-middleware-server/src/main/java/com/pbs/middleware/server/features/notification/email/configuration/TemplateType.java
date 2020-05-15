package com.pbs.middleware.server.features.notification.email.configuration;

import com.google.gson.Gson;
import java.util.Map;
import lombok.Getter;

@Getter
public enum TemplateType {
    /**
     * Email template for customer about successfully finished upload process.
     */
    UPLOAD_SUCCESS("upload/success.ftl"),

    /**
     * Email template for customer about failed upload process.
     */
    UPLOAD_FAIL("upload/fail.ftl"),

    /**
     * Email template for admin user about upload errors.
     */
    UPLOAD_ADMIN_FAIL("upload/fail_admin.ftl"),

    /**
     * Email template for customer about successfully finished download process.
     */
    DOWNLOAD_SUCCESS("download/success.ftl"),

    /**
     * Email template for customer about failed download process.
     */
    DOWNLOAD_FAIL("download/fail.ftl"),

    /**
     * Email template for admin user about download errors.
     */
    DOWNLOAD_ADMIN_FAIL("download/fail_admin.ftl"),

    /**
     * Email template for job notification.
     */
    JOB_NOTIFICATION("job/notification.ftl");

    private String path;

    TemplateType(String path) {
        this.path = path;
    }

    public String getBackup(Map<String, Object> parameters) {
        switch (this) {
            case UPLOAD_SUCCESS:
                return "Upload successfully finished!\n\n\n" + getParameters(parameters);
            case UPLOAD_FAIL:
                return "Upload failed finished!\n\n\n" + getParameters(parameters);
            case UPLOAD_ADMIN_FAIL:
                return "Uploads failed!\n\n\n" + getParameters(parameters);
            case DOWNLOAD_SUCCESS:
                return "Download successfully finished!\n\n\n" + getParameters(parameters);
            case DOWNLOAD_FAIL:
                return "Download failed finished!\n\n\n" + getParameters(parameters);
            case DOWNLOAD_ADMIN_FAIL:
                return "Downloads failed!\n\n\n" + getParameters(parameters);
            case JOB_NOTIFICATION:
                return "Job notification: \n\n\n" + getParameters(parameters);
            default:
                return "";
        }
    }

    private static String getParameters(Map<String, Object> parameters) {
        return new Gson().toJson(parameters);
    }
}
