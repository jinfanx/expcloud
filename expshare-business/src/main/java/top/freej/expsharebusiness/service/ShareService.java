package top.freej.expsharebusiness.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShareService {

    // 确切分类
    int CATEGORY_SPECIFIC = 0;

    // 模糊分类
    int CATEGORY_LIKE = 1;

    List<Object> listSharesByCategory(String category,int queryType) throws Exception;

    List<Object> keywordsSearch(String keywords);

}
