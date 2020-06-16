//package org.speech4j.tenantservice.config.multitenancy;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//
//
//@Component
//public class TenantInterceptor extends HandlerInterceptorAdapter {
////
////    @Override
////    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
////        Map<String, String> pathVariables =
////                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
////        if (pathVariables != null && pathVariables.size() != 0) {
////            TenantContext.setCurrentTenant(pathVariables.get("id"));
////        }
////        return true;
////    }
////
////    @Override
////    public void postHandle(HttpServletRequest request, HttpServletResponse response,
////                           Object handler, ModelAndView modelAndView) {
////        TenantContext.clear();
////    }
//}
