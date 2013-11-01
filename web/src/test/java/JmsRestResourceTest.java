

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import basic.jms.app.rest.IJmsRestResource;

@RunWith(Arquillian.class)
public class JmsRestResourceTest {
  static {RegisterBuiltin.register(ResteasyProviderFactory.getInstance());}

  @Deployment(name = "basicapp", managed = true, testable=true)
  public static Archive<?> createDeployment() {
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "basicapp.war")
        .addPackages(true, "basic.jms.app")
        .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
        .addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml")
        .addAsResource("persistence.xml", "META-INF/persistence.xml")
        .addAsManifestResource("basic-jms-app-ds.xml");
//     System.out.println(archive.toString(true));
    return archive;
  }
  
  @Test
  @RunAsClient
  @SuppressWarnings("rawtypes")
  public void should_return_ok(@ArquillianResource URL url1) throws IOException {
	  IJmsRestResource client = ProxyFactory.create(IJmsRestResource.class, url1
        .toString());
    BaseClientResponse response = (BaseClientResponse)client.send("testQueue", "testMessage");
    assertEquals(200, response.getStatus());
  }

}
