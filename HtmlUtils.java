package com.kyexpress.erp.kuasheng.receive2.provider.component;

import com.kyexpress.framework.utils.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Component;

/**
 * @author chenruisheng
 * @version 1.0
 * @description
 * @date 2023/1/9 16:01
 */
@Component
public class ImRichTextService {

    /**
     * 过滤html标签，并反转义内容中的特殊字符
     * <.*?>为正则表达式，其中的.表示任意字符，*?表示出现0次或0次以上，此方法可以去掉双头标签(双头针对于残缺的标签)
     * "<.*?"表示<尖括号后的所有字符，此方法可以去掉残缺的标签，及后面的内容
     * " "，若有多种此种字符，可用同一方法去除
     */
    public String parseRichText(String content){
        if (StringUtils.isRealEmpty(content)){
            return content;
        }
        //去除标签
        content = content.replaceAll("<.*?>", "");
        //反转义特殊字符
        return StringEscapeUtils.unescapeHtml(content);
    }
}
