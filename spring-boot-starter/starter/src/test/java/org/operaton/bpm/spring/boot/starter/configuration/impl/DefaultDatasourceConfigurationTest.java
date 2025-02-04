/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.operaton.bpm.spring.boot.starter.configuration.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.operaton.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.operaton.bpm.spring.boot.starter.property.OperatonBpmProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDatasourceConfigurationTest {

  @Mock
  private PlatformTransactionManager platformTransactionManager;

  private OperatonBpmProperties operatonBpmProperties;

  @InjectMocks
  private DefaultDatasourceConfiguration defaultDatasourceConfiguration;

  private SpringProcessEngineConfiguration configuration;

  @Before
  public void before() {
    configuration = new SpringProcessEngineConfiguration();
    operatonBpmProperties = new OperatonBpmProperties();
    defaultDatasourceConfiguration.operatonBpmProperties = operatonBpmProperties;
  }

  @Test
  public void transactionManagerTest() {
    defaultDatasourceConfiguration.dataSource = mock(DataSource.class);
    defaultDatasourceConfiguration.preInit(configuration);
    assertSame(platformTransactionManager, configuration.getTransactionManager());
  }

  @Test
  public void operatonTransactionManagerTest() {
    defaultDatasourceConfiguration.dataSource = mock(DataSource.class);
    PlatformTransactionManager operatonTransactionManager = mock(PlatformTransactionManager.class);
    defaultDatasourceConfiguration.operatonTransactionManager = operatonTransactionManager;
    defaultDatasourceConfiguration.preInit(configuration);
    assertSame(operatonTransactionManager, configuration.getTransactionManager());
  }

  @Test
  public void defaultDataSourceTest() {
    DataSource datasourceMock = mock(DataSource.class);
    defaultDatasourceConfiguration.dataSource = datasourceMock;
    defaultDatasourceConfiguration.preInit(configuration);
    assertSame(datasourceMock, getDataSourceFromConfiguration());
  }

  @Test
  public void operatonDataSourceTest() {
    DataSource operatonDatasourceMock = mock(DataSource.class);
    defaultDatasourceConfiguration.operatonDataSource = operatonDatasourceMock;
    defaultDatasourceConfiguration.dataSource = mock(DataSource.class);
    defaultDatasourceConfiguration.preInit(configuration);
    assertSame(operatonDatasourceMock, getDataSourceFromConfiguration());
  }

  private DataSource getDataSourceFromConfiguration() {
    return ((TransactionAwareDataSourceProxy) configuration.getDataSource()).getTargetDataSource();
  }
}
