package basic.jms.app.service.integration;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import basic.jms.app.model.Log;
import basic.jms.app.service.LogBean;

@RunWith(Arquillian.class)
public class LogBeanTest {
  @Inject
  private LogBean bean;

  @Deployment(name = "logtest.jar", managed = true)
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class, "logtest.jar")
        .addClasses(LogBean.class)
        .addPackage("basic.jms.app.model")
        .addAsResource("basic-jms-app-ds.xml", "META-INF/basic-jms-app-ds.xml")
        .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
        .addAsResource("META-INF/beans.xml", "META-INF/beans.xml");
  }

  @Test
  public void should_insert_log() throws IOException {
    Log entity = bean.insert("test");
    assertNotNull(entity);
  }

  @Test
  public void should_find_logs() {
    List<Log> actual = bean.listAll();
    assertNotNull(actual);
  }

}
