package jp.co.fujixerox.nbd.sst;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SstTestBase {
    @Getter
    private TestRestTemplate restTemplate;

    @PersistenceContext
    @Getter
    private EntityManager entityManager;

    @Getter
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @LocalServerPort
    private void createTestRestTemplate(int randomPort) {
        this.restTemplate = new TestRestTemplate(
                new RestTemplateBuilder().requestFactory(
                        new HttpComponentsClientHttpRequestFactory())
                        .rootUri("http://localhost:" + randomPort));
    }

    @Before
    public void setup() throws Exception {
        loadDbUnitDataSet(
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword(),
                dataSourceProperties.getSchema(),
                resourceLoader.getResource("classpath:" + "/dataset.xls").getFile()
        );
    }


    /**
     * @param url
     * @param userName
     * @param password
     * @param schema
     * @param dataset
     * @throws Exception
     */
    private static void loadDbUnitDataSet(
            String url,
            String userName,
            String password,
            String schema,
            File dataset) throws Exception {

        Connection connection = null;
        IDatabaseConnection databaseConnection = null;
        try {
            connection = DriverManager.getConnection(url, userName, password);

            databaseConnection = new DatabaseConnection(connection, schema) {
                @Override
                public DatabaseConfig getConfig() {
                    DatabaseConfig config = super.getConfig();
                    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
                    config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
                    return config;
                }
            };

            IDataSet dataSet = new XlsDataSet(dataset);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (databaseConnection != null) {
                databaseConnection.close();
            }
        }
    }

    protected String loadText(String location) throws IOException {
        return IOUtils.toString(
                getResourceLoader().getResource(location).getInputStream(),
                StandardCharsets.UTF_8
        );
    }
}
