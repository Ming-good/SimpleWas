package org.http.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.resource.ResourceWrite;

public class Dispacher {

    public static ResourceWrite parse(String host, List<HashMap<String, Object>> virtualHostList) throws NoSuchFieldException {
        String documentRoot = null;
        String indexFileNm = null;
        HashMap<String, String> errorPage = null;

        for (Map<String, Object> map : virtualHostList) {
            if (host.equals(map.get("serverNm"))) {
                documentRoot = (String) map.get("documentRoot");
                indexFileNm = (String) map.get("index");
                errorPage = (HashMap<String, String>) map.get("errorPages");
            }
        }

        if (errorPage == null || documentRoot == null) {
            throw new NoSuchFieldException();
        }

        return new ResourceWrite(documentRoot, indexFileNm, errorPage);
    }
}
