import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.ontology.AllDifferent;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.DataRange;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.FunctionalProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.InverseFunctionalProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.ontology.Profile;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.SymmetricProperty;
import org.apache.jena.ontology.TransitiveProperty;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFVisitor;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ontology {

	private static String namespace = "http://www.semanticweb.org/pedro/ontologies/emed.owl#";

	public static void main(String[] args) throws IOException, ParseException {
		InputStream in = new FileInputStream("Emed.owl"); // or any windows path

		OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

		base.read(in, null);
		in.close();

		File dir = new File("bulario/");
		File[] directoryListing = dir.listFiles();

		if (directoryListing != null) {
			for (File child : directoryListing) {
				processFile(base, child);
			}
		}


	}

	public static void processFile(OntModel base, File file) throws FileNotFoundException, IOException, ParseException {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
			name = name.substring(0, pos);
		}

		OntClass MedicamentoClass = base.getOntClass(namespace + "Medicamento");
		ArrayList<OntProperty> MedicamentoProperties = new ArrayList<OntProperty>();

		for (Iterator it = MedicamentoClass.listDeclaredProperties(); it.hasNext();) {
			OntProperty p = (OntProperty) it.next();
			MedicamentoProperties.add(p);
		}

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("bulario/" + name + ".json"));
		JSONObject Medicamento = (JSONObject) obj;

		Individual i = base.createIndividual(namespace + name, MedicamentoClass);

		for (Iterator iterator = Medicamento.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();

			for (OntProperty p : MedicamentoProperties) {
				System.out.println("ugh" + p.getLocalName());

				if (p.getLocalName().equals("nome")) {
					System.out.println("Encontrou a propriedade do nome");
					i.addLiteral(p, Medicamento.get("name"));
				}

				System.out.println(Medicamento.get("uso"));
				if (p.getLocalName().equals("uso"))
					if (Medicamento.get("uso")!= null)
						i.addLiteral(p, Medicamento.get("uso"));
				if (p.getLocalName().equals("posologia"))
					if (Medicamento.get("posologia")!= null)
						i.addLiteral(p, Medicamento.get("posologia"));
				if (p.getLocalName().equals("modoAdministracao"))
					if (Medicamento.get("modo de administração")!= null)
						i.addLiteral(p, Medicamento.get("modo de administração"));
				if (p.getLocalName().equals("grupoFarmacologico"))
					if (Medicamento.get("grupo farmacológico")!= null)
						i.addLiteral(p, Medicamento.get("grupo farmacológico"));
				if (p.getLocalName().equals("efeitoAdverso"))
					if (Medicamento.get("efeitos adversos")!= null)
						i.addLiteral(p, Medicamento.get("efeitos adversos"));
				if (p.getLocalName().equals("contraIndicacao"))
					if (Medicamento.get("contraindicação")!= null)
						i.addLiteral(p, Medicamento.get("contraindicação"));

			}
		}
		
		FileOutputStream fout = new FileOutputStream("teste.rdf");
	       
        //base.write(System.out);
        base.write(fout);
        fout.close();
	}
}
