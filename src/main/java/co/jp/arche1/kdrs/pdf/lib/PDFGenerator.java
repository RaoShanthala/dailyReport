package co.jp.arche1.kdrs.pdf.lib;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.RomanList;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.alignment.VerticalAlignment;

import co.jp.arche1.kdrs.pdf.dto.Employee;

@Component
public class PDFGenerator {

	public static final String DEST = "/Users/shanthala/test/template.pdf";

	public void PDFGenerate() {

		try {
			   Document document = new Document();
			   PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST));
			   //setting font family, color
			   Font font = new Font(Font.HELVETICA, 16, Font.BOLDITALIC, Color.RED);
			   document.open();
			   Paragraph para = new Paragraph("Hello! This PDF is created using openPDF", font);
			   para.setLeading(40, 2); // creates empty lines above the paragraph
			   para.setAlignment(Element.ALIGN_CENTER); // aligns text at the center

			   //The space between two lines of the same Paragraph is called the leading. Changing text line spacing
			   //https://stackoverflow.com/questions/39690237/how-to-easily-control-spacing-height-between-two-paragraphs
			   //If you want to introduce extra spacing before or after a Paragraph, you can use the setSpacingBefore() or setSpacingAfter() method.
			   //See itext spacingBefore property applied to Paragraph causes new page


			   //This puts 72 user units of extra white space between paragraph1 and paragraph2.
			   //One user unit corresponds with one point, so by choosing 72, we've added an inch of white space.
			   para.setSpacingAfter(72f);
			   document.add(para);

		//	   document.add( Chunk.NEWLINE );


			   document.add(new Paragraph("List with Numbers"));
			    List list = new List(List.ORDERED);
			    list.setIndentationLeft(15);
			    list.add(new ListItem("Item1"));
			    list.add(new ListItem("Item2"));
			    list.add(new ListItem("Item3"));

			    document.add(list);

			    document.add(new Paragraph("List with Alphabets Uppercase"));
			    list = new List(false, List.ALPHABETICAL);
			    list.setIndentationLeft(15);
			    list.add(new ListItem("Item1"));
			    list.add(new ListItem("Item2"));
			    list.add(new ListItem("Item3"));
			    document.add(list);

			    document.add(addSpace(20)); // to add line break

			    document.add(new Paragraph("List with Roman Numerals"));
			    List romanList = new RomanList(List.UPPERCASE, 14);
			    // Add ListItem objects
			    romanList.add(new ListItem("Item1"));
			    romanList.add(new ListItem("Item2"));
			    romanList.add(new ListItem("Item3"));

			    document.add(romanList);

			    document.add(new Paragraph("List with Nested List"));
			    list = new List(false, List.ALPHABETICAL);
			    list.setIndentationLeft(15);
			    list.add(new ListItem("Item1"));
			    // Nested List
			    List nestedList = new List();
			    nestedList.setIndentationLeft(20);
			    nestedList.setListSymbol("\u2022");
			    nestedList.add(new ListItem("Item2"));
			    nestedList.add(new ListItem("Item3"));
			    list.add(nestedList);
			    list.add(new ListItem("Item4"));
			    document.add(list);

			    font = new Font(Font.HELVETICA, 12, Font.BOLD);
			      Table table = new Table(3);
			      table.setPadding(5);
			    //  table.setSpacing(1);
			      table.setWidth(100);
			      // Setting table headers
			      Cell cell = new Cell("Employee Information");
			      cell.setHeader(true);
			      cell.setVerticalAlignment(VerticalAlignment.CENTER);
			      cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			      cell.setColspan(3);
			      cell.setBackgroundColor(Color.LIGHT_GRAY);
			      table.addCell(cell);

			      table.addCell(new Phrase("Name", font));
			      table.addCell(new Phrase("Dept", font));
			      table.addCell(new Phrase("Salary", font));
			      table.endHeaders();
			      // Employee information to table cells
			      ArrayList<Employee> employees = getEmployees();
			      for(Employee emp : employees) {
			        table.addCell(emp.getName());
			        table.addCell(emp.getDept());
			        table.addCell(Integer.toString(emp.getSalary()));
			      }

			      document.add(table);

			      Chunk glue = new Chunk(new VerticalPositionMark());
			      Paragraph p = new Paragraph();
			      p.add("Text to the left");
			      p.add(glue);
			      p.add("Text to the right");

			      document.add(p);

			      //adding image to the pdf

			      Image img = Image.getInstance("/Users/shanthala/kdrs/Nature.jpg");
			      img.scaleToFit(100,100); // controls the picture size
			      img.setAbsolutePosition(50,550); // positioning x and y
			      img.setAlignment(Element.ALIGN_CENTER);
			      document.add(img);


			    document.close();
			   writer.close();
			  } catch ( Exception e ) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  }

	}

	// create a list of employees
	  private static ArrayList<Employee> getEmployees() {
	    ArrayList<Employee> employees = new ArrayList<>();
	    employees.add(new Employee("Jack", "HR", 12000));
	    employees.add(new Employee("Liza", "IT", 5000));
	    employees.add(new Employee("Jeremy", "Finance", 9000));
	    employees.add(new Employee("Frederick", "Accounts", 8000));
	    return employees;
	  }

	  private static Paragraph addSpace(int size){

	        Font LineBreak = FontFactory.getFont("Arial", size);
	        Paragraph paragraph = new Paragraph("\n\n", LineBreak);
	        return paragraph;

	    }

}
