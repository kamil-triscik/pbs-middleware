package com.pbs.middleware.common.pbs;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chunk {

    private String id;

    private Long count;

    private Long ncpus;

    private String mem;

    private Map<String, String> customResources;

    public String toPbsString(final String separator, final String assign) {

        final String count_ = this.count != null && this.count > 0L ? this.count.toString() : "1";
        final String ncpus_ = this.ncpus != null && this.ncpus > 0L ? "ncpus" + assign + this.ncpus : "";
        final String mem_ = this.mem != null && !this.mem.isEmpty() ? "mem" + assign + this.mem : "";
        final String cs = this.customResourcesToString(separator, assign);

        return merge(separator, count_, ncpus_, mem_, cs);

    }

    private String customResourcesToString(final String separator, final String assign) {
        if (this.getCustomResources() != null) {
            return this.getCustomResources()
                    .keySet()
                    .stream()
                    .map(key -> format("%s%s%s", key, assign, this.getCustomResources().get(key)))
                    .collect(joining(separator));
        }
        return "";
    }

    private String merge(final String separator, String... params) {
        StringBuilder result = new StringBuilder();

        for (String param : params) {
            if (result.length() == 0) {
                result = new StringBuilder(param);
                continue;
            }
            if (param.isEmpty()) {
                continue;
            }
            result.append(separator).append(param);
        }

        return result.toString();
    }
}