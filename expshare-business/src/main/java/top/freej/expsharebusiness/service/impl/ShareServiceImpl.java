package top.freej.expsharebusiness.service.impl;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import top.freej.expsharebusiness.Util.ObjectIdUtil;
import top.freej.expsharebusiness.dao.CommonDao;
import top.freej.expsharebusiness.service.ShareService;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ShareServiceImpl implements ShareService {

    @Resource
    private CommonDao commonDao;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<Object> listSharesByCategory(String category,int queryType) throws Exception {
        Query query = new Query();

        if (queryType == ShareService.CATEGORY_SPECIFIC){
            query.addCriteria(Criteria.where("category").is(category));
        } else if (queryType == ShareService.CATEGORY_LIKE){
            Pattern pattern = Pattern.compile("^"+category+"*");
            query.addCriteria(Criteria.where("category").regex(pattern));
        } else {
            throw new Exception("分类查询：未知的分类等级["+queryType+"]");
        }
        return ObjectIdUtil.convertObjectId(commonDao.queryObjectList("share",query));
    }

    /**
     * 关键字搜索
     * @param keywords
     * @return
     */
    @Override
    public List<Object> keywordsSearch(String keywords) {

        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(keywords);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withIndices("expshare")
                .withTypes("share")
                .withQuery(queryBuilder)
                .withHighlightFields(new HighlightBuilder.Field("description"),
                        new HighlightBuilder.Field("reason"),
                        new HighlightBuilder.Field("solution"))
                .build();
        List<Object> result = elasticsearchTemplate.queryForList(query,Object.class);
        return ObjectIdUtil.convertObjectId(result);
    }
}
