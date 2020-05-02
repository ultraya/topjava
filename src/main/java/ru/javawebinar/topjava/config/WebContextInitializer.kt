package ru.javawebinar.topjava.config

import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer

//https://www.baeldung.com/spring-web-contexts
/*
The WebApplicationInitializer class that we've seen earlier is a general-purpose interface.
It turns out that Spring provides a few more specific implementations, including an
abstract class called AbstractContextLoaderInitializer
 */
//class WebConfig : WebApplicationInitializer {
//    override fun onStartup(context: ServletContext) {
//        val rootContext = AnnotationConfigWebApplicationContext()
//        rootContext.register(JavaConfig::class.java)
//        container.addListener(ContextLoaderListener(rootContext))
//
//        это контекст диспатчер сервлета - разделили только для удобства(см описание WebApplicationInitializer)
//        AnnotationConfigWebApplicationContext dispatcherContext =
//        new AnnotationConfigWebApplicationContext();
//        dispatcherContext.register(WebConfig.class);
//
//        ServletRegistration.Dynamic dispatcher =
//              context.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
//
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");

//    }
//}

//without mvc, only web context and servlet
//class WebContextInitializer : AbstractContextLoaderInitializer() {
//    override fun createRootApplicationContext(): WebApplicationContext =
//            AnnotationConfigWebApplicationContext().apply {
//                register(CoreConfig::class.java, DaoConfig::class.java)
//            }
//}
/*
the root context is the parent of every dispatcher servlet context.
Thus, beans defined in the root web application context are visible to each dispatcher servlet context,
but not vice versa.
 */
class WebContextInitializer : AbstractDispatcherServletInitializer() {

    override fun createRootApplicationContext(): WebApplicationContext =
            AnnotationConfigWebApplicationContext().apply {
                register(CoreConfig::class.java, DaoConfig::class.java)
                environment.setActiveProfiles("postgres", "datajpa")
            }

    override fun getServletMappings() = arrayOf("/") // not use /* - then not found view

    override fun createServletApplicationContext(): WebApplicationContext =
            AnnotationConfigWebApplicationContext().apply {
                register(WebConfig::class.java)
            }
}