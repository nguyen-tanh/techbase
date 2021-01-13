package com.techbase.support.binding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author nguyentanh
 */
public class PagerArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(PagerArgumentResolver.class);

    private final int defaultSize;

    public PagerArgumentResolver(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    /**
     * Only support if parameter have annotation {@link Pager} and type must {@link PageRequest}
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethodAnnotation(Pager.class) != null && PageRequest.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        Pager pagerAnn = parameter.getMethodAnnotation(Pager.class);
        if (pagerAnn == null) throw new UnsupportedOperationException("@Pager is not found");

        // page
        String pageParam = webRequest.getParameter(pagerAnn.page());
        int page = this.createPage(pageParam, pagerAnn);

        // size
        String sizeParam = webRequest.getParameter(pagerAnn.size());
        int size = this.createSize(sizeParam, pagerAnn);

        return PageRequest.of(page, size);
    }

    /**
     * Create page value
     * <p>
     * If page in query string is wrong, it return first page
     */
    private int createPage(String pageParam, Pager pagerAnn) {
        int page = 0;

        try {
            page = (pageParam != null) ? Integer.parseUnsignedInt(pageParam) : 0;
            if (page >= 1) page = page - 1; // @page zero-based page index, must not be negative.
        } catch (NumberFormatException e) {
            LOGGER.warn("Param [{}] = [{}] is incorrect", pagerAnn.page(), pageParam);
        }

        return page;
    }

    /**
     * Create size value
     * <p>
     * If size in query string is wrong, it return default value
     */
    private int createSize(String sizeParam, Pager pagerAnn) {
        int size = defaultSize;

        try {
            size = (sizeParam != null) ? Integer.parseInt(sizeParam) : size;
            if (size < 1 || size > defaultSize) size = defaultSize;
        } catch (NumberFormatException e) {
            LOGGER.warn("Param [{}] = [{}] is incorrect", pagerAnn.size(), sizeParam);
        }
        return size;
    }
}
