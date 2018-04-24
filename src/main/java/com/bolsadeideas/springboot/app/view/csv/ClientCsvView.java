package com.bolsadeideas.springboot.app.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.bolsadeideas.springboot.app.models.entity.Client;

@Component("list.csv")
public class ClientCsvView extends AbstractView{

	public ClientCsvView() {
		setContentType("text/csv");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		final String filename = "clients.csv";
		res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		res.setContentType(getContentType());

		Page<Client> clients = (Page<Client>) model.get("clients");
		ICsvBeanWriter beanWriter = new CsvBeanWriter(res.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		// Nombres EXACTOS de las propiedades de los objetos a escribir,
		// en este caso clientes
		String[] headers = {"id", "name", "surname", "email", "createdAt"};
		beanWriter.writeHeader(headers);
		
		for(Client client : clients) {
			beanWriter.write(client, headers);
		}
		beanWriter.close();
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}


}
