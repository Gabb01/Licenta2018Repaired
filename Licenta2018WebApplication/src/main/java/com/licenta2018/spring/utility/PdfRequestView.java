package com.licenta2018.spring.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.licenta2018.spring.model.Request;
import com.licenta2018.spring.model.User;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfRequestView {

	public static ByteArrayInputStream generatePdf(User user, Request request)
	{
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date today = new Date();
			
			/*
			 * Header
			 */
			Paragraph dest;
			if(request.getType().getDescription().equals("Autorizatie constructie"))
				dest = new Paragraph(new Chunk("Departamentul de constructii ePrimarie Iasi", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
			else if(request.getType().getDescription().equals("Autorizatie mediu"))
				dest = new Paragraph(new Chunk("Departamentul de mediu ePrimarie Iasi", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
			else
				dest = new Paragraph(new Chunk("Departamentul directiei economice ePrimarie Iasi", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
			dest.setAlignment(Element.ALIGN_LEFT);
			
			Paragraph date = new Paragraph(new Chunk("Data: " + dateFormat.format(today)));
			date.add(Chunk.NEWLINE);
			date.add(Chunk.NEWLINE);
			date.add(Chunk.NEWLINE);
			date.setAlignment(Element.ALIGN_RIGHT);
			
			Paragraph header = new Paragraph(new Chunk(request.getType().getDescription(), FontFactory.getFont(FontFactory.TIMES_BOLD, 25)));
			header.setAlignment(Element.ALIGN_CENTER);
			header.add(Chunk.NEWLINE);
			header.add(Chunk.NEWLINE);
			header.add(Chunk.NEWLINE);
			header.add(Chunk.NEWLINE);
			
			/*
			 * Body
			 */
			
			Paragraph container = new Paragraph("       Ca urmare a cererii adresate de " + user.getName() + " in calitate de reprezentant al " 
			+ request.getNumeSolicitant() + " cu sediul in " + request.getAdresaConstructie() + " inregistrata cu nr " + request.getId() + " la data de " 
			+ new SimpleDateFormat("HH:mm dd/MM/yyyy").format(request.getIssueDate())+".", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13));
			container.add(Chunk.NEWLINE);
			container.add(Chunk.NEWLINE);
			
			if(request.getType().getId() == 1)
			{
				container.add(new Paragraph("Se autorizeaza executia lucrarilor: " + request.getCategorieConstructie() + " pe o suprafata de " + request.getSuprafataTeren() + ". Valoarea lucrarilor de construire se ridica la "
					+request.getValoareLucrari() + "."));
				container.add(Chunk.NEWLINE);
				container.add(new Paragraph("Documentatia de proiect a fost elaborata de proiectantul " + request.getNumeProiectant()));
				container.add(Chunk.NEWLINE);
				container.add(Chunk.NEWLINE);
				container.add(new Paragraph("Termenul de incepere a lucrarilor de constructie este de 2 luni de la data eliberarii prezentei autorizatii."));
				container.add(Chunk.NEWLINE);
				container.add(Chunk.NEWLINE);
				container.setFont(new Font(Font.TIMES_ROMAN, 13));
				container.setAlignment(Element.ALIGN_JUSTIFIED);
			}
			else if(request.getType().getId() == 2)
			{
				container.add(new Paragraph("Se elibereaza prezenta autorizatie pentru " + request.getNumeSolicitant()+ " cu sediul la adresa " + request.getAdresaConstructie() + " care prevedere desfasurarea urmatoarei activitati: "
						+request.getCodCaen() + " - " + request.getCategorieConstructie() + "."));
					container.add(Chunk.NEWLINE);
					container.add(Chunk.NEWLINE);
					container.add(new Paragraph("Amplasamentul are o suprafata totala de " + request.getSuprafataTeren() + "."));					
					container.add(Chunk.NEWLINE);
					container.add(Chunk.NEWLINE);
					container.add(Chunk.NEWLINE);
					container.setFont(new Font(Font.TIMES_ROMAN, 13));
					container.setAlignment(Element.ALIGN_JUSTIFIED);
			}
			else
			{
				container.add(new Paragraph("Se autorizeaza societatii comerciale: " + request.getNumeSolicitant() + " cu sediul in " + request.getAdresaConstructie() + 
						" sa desfasoare activitati, clasa CAEN: " + request.getCodCaen() + " - " + request.getCategorieConstructie() + "."));
					container.add(Chunk.NEWLINE);
					container.add(new Paragraph("Mentiune: Autorizatia este valabila cu respectarea conditilor prevazute de lege si va fi vizata anual."));
					container.add(Chunk.NEWLINE);
					container.add(Chunk.NEWLINE);
					container.add(new Paragraph("Societatea comerciala functioneaza cu autorizatia de securitate la incendiu cu nr "+ request.getNrAutorizatie() + "."));
					container.add(Chunk.NEWLINE);
					container.add(Chunk.NEWLINE);
					container.setFont(new Font(Font.TIMES_ROMAN, 13));
					container.setAlignment(Element.ALIGN_JUSTIFIED);
			}
			
			/*
			 * Footer
			 */
			
			Paragraph footer = new Paragraph(new Chunk("Data aprobarii: " + new SimpleDateFormat("HH:mm dd/MM/yyyy").format(request.getApprovalDate()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 13)));
			footer.setAlignment(Element.ALIGN_LEFT);
			Paragraph signature = new Paragraph(new Chunk("Semnatura,", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13)));
			signature.add(Chunk.NEWLINE);
			if(request.getType().getId() == 1)
				signature.add(new Paragraph("Departament constructii"));
			else if(request.getType().getId() == 1)
				signature.add(new Paragraph("Departament mediu"));
			else 
				signature.add(new Paragraph("Directia Economica"));
			signature.setAlignment(Element.ALIGN_RIGHT);
			
			PdfWriter.getInstance(document, out);
			document.open();
			document.add(dest);
			document.add(date);
			document.add(header);
			document.add(container);
			document.add(footer);
			document.add(signature);
			document.close();
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

}
