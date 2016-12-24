package test.test6_type_safe;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.github.drinkjava2.BeanBox;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * This configuration do same thing like example5, but use Java type safe configurations to create bean instance and
 * inject values. <br/>
 * 
 * @author Yong Zhu
 * @since 2016-8-23
 * @update 2016-8-23
 */
public class TesterBox extends BeanBox {
	static {
		BeanBox.defaultContext.close();// clean up
		BeanBox.defaultContext.setAOPAround("test.test\\w*.Test\\w*", "insert\\w*", new TxInterceptorBox(), "invoke");
	}

	static class DSPoolBeanBox extends BeanBox {// Type-unsafe and type-safe configurations can mixed use.
		public ComboPooledDataSource create() {
			ComboPooledDataSource ds = new ComboPooledDataSource();
			ds.setUser("root");
			return ds;
		}

		public void config(ComboPooledDataSource ds) {
			ds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test");
			ds.setPassword("root888");// change to your PWD
			ds.setCheckoutTimeout(2000);
		}

		{
			setProperty("driverClass", "com.mysql.jdbc.Driver");
		}

	}

	static class TxManagerBox extends BeanBox {
		public DataSourceTransactionManager create() {
			DataSourceTransactionManager dm = new DataSourceTransactionManager();
			dm.setDataSource((DataSource) getContext().getBean(DSPoolBeanBox.class));
			return dm;
		}
	}

	static class TxInterceptorBox extends BeanBox {// Advice
		public TransactionInterceptor create() {
			Properties props = new Properties();
			props.put("insert*", "PROPAGATION_REQUIRED");
			return new TransactionInterceptor((DataSourceTransactionManager) getContext().getBean(TxManagerBox.class),
					props);
		}
	}

	public static class JdbcTemplateBox extends BeanBox {
		public JdbcTemplate create() {
			return new JdbcTemplate((DataSource) getContext().getBean(DSPoolBeanBox.class));
		}
	}

}
