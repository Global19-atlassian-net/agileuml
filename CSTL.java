import java.io.*;
import java.util.Vector; 


public class CSTL
{ // All *.cstl files in output directory are loaded

  static java.util.Map templates = new java.util.HashMap();

  public static void loadTemplates(Vector types, Vector entities)
  { File dir = new File("./cg");
    String[] dirfiles = dir.list();
    for (int i = 0; i < dirfiles.length; i++)
    { File sub = new File("./cg/" + dirfiles[i]);
      if (sub != null && dirfiles[i].endsWith(".cstl") && 
          !(dirfiles[i].equals("cg.cstl")))
      { System.out.println(">>> Found CSTL template: " + dirfiles[i]); 
        CGSpec cg = loadCSTL(sub,types,entities); 
        if (cg != null) 
        { addTemplate(dirfiles[i],cg); }         
      }
    }
  }

  public static CGSpec loadCSTL(File file, Vector types, Vector entities)
  { CGSpec res = new CGSpec(); 
    
    BufferedReader br = null;
    String s;
    boolean eof = false;
    
    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException _e)
    { System.err.println("!! ERROR: File not found: " + file);
      return null; 
    }

    int noflines = 0; 
    String mode = "none"; 

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException _ex)
      { System.out.println("ERROR!!: Reading CSTL file failed.");
        return null; 
      }
      if (s == null) 
      { eof = true; 
        break; 
      }
      else if (s.startsWith("--")) { } 
      else if (s.trim().length() == 0) { } 
      else if (s.equals("Type::"))
      { mode = "types"; }         
      else if (s.equals("Enumeration::"))
      { mode = "enumerations"; }         
      else if (s.equals("Text::"))
      { mode = "texts"; }         
      else if (s.equals("BasicExpression::"))
      { mode = "basicexpressions"; }  
      else if (s.equals("UnaryExpression::"))
      { mode = "unaryexpressions"; }  
      else if (s.equals("BinaryExpression::"))
      { mode = "binaryexpressions"; }  
      else if (s.equals("SetExpression::"))
      { mode = "setexpressions"; }  
      else if (s.equals("ConditionalExpression::"))
      { mode = "conditionalexpressions"; }  
      else if (s.equals("Class::"))
      { mode = "entities"; }  
      else if (s.equals("Attribute::"))
      { mode = "attributes"; }  
      else if (s.equals("Parameter::"))
      { mode = "parameters"; }  
      else if (s.equals("Operation::"))
      { mode = "operations"; }  
      else if (s.equals("Statement::"))
      { mode = "statements"; }  
      else if (s.equals("Package::"))
      { mode = "packages"; }  
      else if (s.equals("UseCase::"))
      { mode = "usecases"; }  
      else if ("types".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_TypeCodegenerationrule(s.trim());
        if (r != null) 
        { res.addTypeUseRule(r); } 
      }  
      else if ("basicexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addBasicExpressionRule(r); } 
      }  
      else if ("unaryexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addUnaryExpressionRule(r); } 
      }  
      else if ("binaryexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addBinaryExpressionRule(r); } 
      }  
      else if ("setexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addSetExpressionRule(r); } 
      }  
      else if ("conditionalexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addConditionalExpressionRule(r); } 
      }  
      else if ("entities".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_EntityCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addClassRule(r); } 
      }         
      else if ("enumerations".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_EntityCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addEnumerationRule(r); } 
      }         
      else if ("packages".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_EntityCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addPackageRule(r); } 
      }         
      else if ("attributes".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_AttributeCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addAttributeRule(r); } 
      }         
      else if ("operations".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_OperationCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addOperationRule(r); } 
      }  
      else if ("parameters".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_OperationCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addParameterRule(r); } 
      }       
      else if ("statements".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_StatementCodegenerationrule(s.trim(),entities,types); 
        if (r != null) 
        { res.addStatementRule(r); } 
      }         
      else if ("usecases".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_UseCaseCodegenerationrule(s.trim(),entities,types); 
        if (r != null) 
        { res.addUseCaseRule(r); } 
      }         
      else if ("texts".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_TextCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addTextRule(r); } 
      }         
    }
    System.out.println(">>> Parsed: " + res); 
    return res; 
  }


  public static void addTemplate(String filename, CGSpec cg) 
  { templates.put(filename,cg); }

  public static boolean hasTemplate(String filename)
  { CGSpec cg = (CGSpec) templates.get(filename); 
    if (cg != null) 
    { return true; } 
    return false; 
  } 

  public static CGSpec getTemplate(String name)
  { return (CGSpec) templates.get(name); }
}

