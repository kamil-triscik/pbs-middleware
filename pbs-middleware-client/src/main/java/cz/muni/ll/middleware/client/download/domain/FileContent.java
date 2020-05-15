package cz.muni.ll.middleware.client.download.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileContent {

    private byte[] data;

    private String filename;

    public long getSize() {
        return data.length;
    }

}
