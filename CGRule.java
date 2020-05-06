/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Code Generation */ 

import java.util.Vector; 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 


public class CGRule
{ String lhs;
  String rhs;
  Vector variables; // The _i in lhs -- 
                    // no additional _i should be in rhs
  Vector metafeatures; // The _i`f in rhs
  String lhsop = "";
  Vector conditions;
  String lhspattern = ""; // The LHS string as a regex pattern
  Vector lhspatternlist = new Vector(); 

  public CGRule(Expression lexp, Expression rexp, Vector whens)
  { Vector lvars = lexp.metavariables();
    Vector rvars = rexp.metavariables();
    
    if (lvars.containsAll(rvars)) {}
    else
    { System.err.println("!! Error: some extra metavariables on RHS of " + lexp + " |--> " + rexp); }
    lhs = lexp + "";
    rhs = rexp + "";
    variables = lvars;
    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Expression lexp, String rgt, Vector whens)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Expression lexp, String rgt)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = new Vector();
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Statement lexp, String rgt, Vector whens)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Statement lexp, String rgt)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = new Vector();
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Type lexp, String rgt, Vector whens)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }


  public CGRule(Type lexp, String rgt)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = new Vector();
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(String ls, String rs, Vector vs, Vector whens)
  { lhs = ls;
    rhs = rs;
    variables = vs;

    Vector rvariables = metavariables(rs); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public int variableCount()
  { if (variables == null) 
    { return 0; } 
    return variables.size(); 
  } 

  public boolean hasVariables()
  { return variables != null && variables.size() > 0; } 

  public static Vector metavariables(String str) 
  { Vector res = new Vector(); 
    for (int i = 1; i < 10; i++) 
    { String var = "_" + i; 
      if (str.indexOf(var) > -1) 
      { res.add(var); } 
    } 
    return res; 
  } 

  public static Vector metafeatures(String str) 
  { Vector res = new Vector(); 
    for (int i = 1; i < 10; i++) 
    { String var = "_" + i + "`"; 
      if (str.indexOf(var) > -1) 
      { int j = str.indexOf(var); 
        String f = var; 
        System.out.println(">>> found metafeature for " + var); 

        boolean found = false; 
        for (int k = j+3; k < str.length() && !found; k++) 
        { if (Character.isLetter(str.charAt(k)))
          { f = f + str.charAt(k); } 
          else 
          { res.add(f);
            found = true; 
          } 
        } 
      } 
    } 
    return res; 
  } // look for identifier starting from the `

  public String toString() 
  { String res = lhs + " |-->" + rhs; 
    if (conditions != null && conditions.size() > 0) 
    { res = res + "<when> "; 
      for (int i = 0; i < conditions.size(); i++) 
      { CGCondition cnd = (CGCondition) conditions.get(i); 
        res = res + cnd;
        if (i < conditions.size() - 1) 
        { res = res + ", "; }
      } 
    }
    return res;  
  } 
 

  public void addCondition(CGCondition cond)
  { conditions.add(cond); }

  public boolean hasCondition(String prop)
  { for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equals(cond.stereotype) && cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasNegativeCondition(String prop)
  { for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equals(cond.stereotype) && !cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasCondition(String prop, String var)
  { for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equals(cond.stereotype) && var.equals(cond.variable) && cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean satisfiesConditions(Vector args)
  { return CGCondition.conditionsSatisfied(conditions,args); } 

  public String applyRule(Vector args)
  { // substitute variables[i] by args[i] in rhs
    String res = rhs + "";
    for (int x = 0; x < args.size() && x < variables.size(); x++)
    { String var = (String) variables.get(x);
      String arg = (String) args.get(x);
      String arg1 = correctNewlines(arg); 
      // System.out.println(">--> Replacing " + var + " by " + arg1); 
      res = res.replaceAll(var,arg1);
    }
    return res;
  }

  public String applyRule(Vector args, Vector eargs, CGSpec cgs)
  { // substitute metafeatures[j] by the value of eargs[j] metafeature
    // substitute variables[i] by args[i] in rhs
    
    String res = rhs + "";
    for (int j = 0; j < metafeatures.size(); j++) 
    { String mf = (String) metafeatures.get(j); 
      String mfvar = mf.substring(0,2); 
      String mffeat = mf.substring(3,mf.length());
      int k = Integer.parseInt(mfvar.charAt(1) + "");  
      if (k >= 1 && k <= eargs.size())
      { Object obj = eargs.get(k-1);
        // System.out.println(">>> Applying metafeature " + mffeat + " to " + obj); 


        if ("defaultValue".equals(mffeat) && obj instanceof Type)
        { Type ee = (Type) obj; 
          Expression exp = ee.getDefaultValueExpression(); 
          if (exp != null) 
          { String repl = exp.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        }
        else if ("elementType".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing metafeature " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        }
        else if ("elementType".equals(mffeat) && obj instanceof Type)
        { Type ee = (Type) obj; 
          Type t = ee.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        }
        else if ("elementType".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if ("type".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        }
        else if ("type".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if ("type".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if ("typename".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replaceAll(mf,repl);
          } 
        }
        else if ("typename".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replaceAll(mf,repl);
          } 
        } 
        else if ("typename".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replaceAll(mf,repl);
          } 
        } 
        else if ("elementType".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature bf = (BehaviouralFeature) obj; 
          Type t = bf.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        }
        else if ("owner".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Entity et = att.getOwner(); 
          if (et != null) 
          { String repl = et.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if ("owner".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Entity et = e.getOwner(); 
          if (et != null) 
          { String repl = et.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if ("ownername".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Entity et = att.getOwner(); 
          if (et != null) 
          { String repl = et.getName(); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if ("ownername".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Entity et = e.getOwner(); 
          if (et != null) 
          { String repl = et.getName(); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if ("name".equals(mffeat) && obj instanceof ModelElement)
        { ModelElement e = (ModelElement) obj; 
          String repl = e.getName(); 
          if (repl != null) 
          { String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replaceAll(mf,repl1);
          } 
        } 
        else if (CSTL.hasTemplate(mffeat + ".cstl")) 
        { CGSpec template = CSTL.getTemplate(mffeat + ".cstl"); 
          System.out.println(">>> Applying template " + mffeat + " to " + obj); 

          String repl = null; 
          if (obj instanceof ModelElement)
          { ModelElement e = (ModelElement) obj; 
            repl = e.cg(template);
          } 
          else if (obj instanceof Vector)
          { Vector v = (Vector) obj;
            repl = "";  
            for (int p = 0; p < v.size(); p++) 
            { ModelElement kme = (ModelElement) v.get(p); 
              repl = repl + kme.cg(template); 
            } 
          } 

          if (repl != null) 
          { String repl1 = correctNewlines(repl);
            res = res.replaceAll(mf,repl1); 
          }  // _1`file for template file.cstl
        } 
      } 
    }

    // Should check for satisfaction of conditions *after* such substitutions 

    // Extend this to allow users to define their own metafeatures in the specification
    // def: _x`f = _x.expr for some abstract syntax OCL expr. 
 

    for (int x = 0; x < args.size() && x < variables.size(); x++)
    { String var = (String) variables.get(x);
      String arg = (String) args.get(x);
      String arg1 = correctNewlines(arg); 
      // System.out.println(">--> Replacing " + var + " by " + arg1); 
      res = res.replaceAll(var,arg1);
    }
    return res;
  }
  
  public String applyTextRule(String actualText)
  { String res = "" + rhs; 
    // lhspattern = convertToPattern(lhs); 
    lhspatternlist = convertToPatterns(lhs);
	 
    // Pattern expr = Pattern.compile(lhspattern); 

    // Matcher m = expr.matcher(actualText); 

    // boolean found = m.find(); 
	  
    /* if (found)
    { int c = m.groupCount(); 
      // System.out.println(m);
	  
      for (int x = 0; x+1 <= c && x < variables.size(); x++)
      { String var = (String) variables.get(x);
        String arg = m.group(x+1);
        // String arg1 = correctNewlines(arg); 
        System.out.println(">--> Replacing " + var + " by " + arg); 
        res = res.replaceAll(var,arg);
      }
    } 
	else */ 
  
     Vector matchings = new Vector(); 
	 boolean found = checkPatternList(actualText,matchings); 
	 if (found && matchings.size() >= variableCount() && matchings.size() > 0) 
	 { System.out.println(">-->> Match of " + actualText + " to " + lhspatternlist);
	   for (int i = 0; i < variables.size(); i++) 
	   { String var = variables.get(i) + ""; 
	     String arg = (String) matchings.get(i); 
		 System.out.println(">--> Replacing " + var + " by " + arg); 
         res = res.replaceAll(var,arg);
      } 
	}  
	
    return res;
  }

  public static String correctNewlines(String str) 
  { String res = ""; 
    if (str.length() == 0) 
    { return res; } 

    for (int i = 0; i < str.length() - 1; i++) 
    { char c1 = str.charAt(i); 
      char c2 = str.charAt(i+1); 
      if (c1 == '\\' && c2 == 'n')
      { res = res + '\n'; 
        i++;
        if (i == str.length() - 1)
        { return res; }  
      } 
      else 
      { res = res + c1; } 
    } 
    return res + str.charAt(str.length()-1); 
  } 
  
  public static Vector convertToPatterns(String str)
  { Vector res = new Vector(); 
    
    String fres = ""; 
    for (int i = 0; i < str.length(); i++) 
    { char c1 = str.charAt(i); 
	  if (i == str.length() - 1)
	  { fres = fres + c1;
	    res.add(fres);  
	    break; 
	  }
      char c2 = str.charAt(i+1); 
      if (c1 == '_' && 
          (c2 == '1' || c2 == '2' || c2 == '3' || c2 == '4' 
           || c2 == '5' || c2 == '6' || c2 == '7' ||
           c2 == '8' || c2 == '9'))
      { res.add(fres);  
	    res.add(("" + c1) + c2); 
	    fres = ""; 
        i++; 
      } 
      else 
      { fres = fres + c1; } 
    } 
	System.out.println("String list = " + res); 
	return res; 
  }
  
  public boolean checkPatternList(String text, Vector matched)
  { if (lhspatternlist == null)
    { return false; } 
  
    if (lhspatternlist.size() == 0)
    { return false; } 
  
    int pos = 0; 
    int i = 0;  
	
    while (i < lhspatternlist.size()) 
    { String tomatch = (String) lhspatternlist.get(i);
	  // System.out.println(">> matching " + tomatch); 
	  
	 if (tomatch.indexOf("_") >= 0)  // variable
	 { String found = ""; 
	   boolean continuematch = true; 
		
	   if (i < lhspatternlist.size() - 1)
	   { char startnext = ((String) lhspatternlist.get(i+1)).charAt(0); 
		int j = pos; 
		while (j < text.length() && continuematch)
		{ if (text.charAt(j) != startnext)
		  { found = found + text.charAt(j);
		    j++;
		  }
	        else if (found.length() > 0)
		   { System.out.println("--> Found text " + found + " for " + tomatch);
			pos = j;  
			matched.add(found); 
			continuematch = false; 
	        }
		   else 
		   { return false; }
		 }
		 i++; 
		}
		else // for the last text segment in the list
		{ for (int j = pos; j < text.length(); j++)
		  { found = found + text.charAt(j); }
		 
		  if (found.length() > 0) 
		  { System.out.println("--> Found text " + found + " for " + tomatch);
		    matched.add(found); 
		    return true;
		  } 
		  else 
		  { return false; } 
		}
	  }
	  else 
	  { int spos = 0;  
	    boolean continuematch = true; 
	    int j = pos; 
		while (j < text.length() && continuematch) 
	    { char x = text.charAt(j); 
	      if (spos < tomatch.length())
		  { if (tomatch.charAt(spos) == x) 
            { spos++; 
		      pos++;
		      j++; 
			  // System.out.println("Consumed " + x);
			} 
			else 
			{ // System.out.println("Mismatch: " + tomatch.charAt(spos) + " /= " + x); 
			  return false; 
			}  
		  }
		  else // go to next segment to match 
		  { j++;  
		    // matched.add(tomatch); 
		    continuematch = false;  
		  }
		}
		i++; 
	  }
	  // i++; 
	}
	// System.out.println(">>> Match list= " + matched); 
	return true; 
  } 


  public static String convertToPattern(String str) 
  { String res = ""; 
    if (str.length() == 0) 
    { return res; } 

    for (int i = 0; i < str.length(); i++) 
    { char c1 = str.charAt(i); 
	  if (c1 == '(')
	  { res = res + "\\("; }
	  else if (c1 == ')')
	  { res = res + "\\)"; }
      else if (c1 == '[' || c1 == ']' ||
          c1 == '*' || c1 == '.' || c1 == '?' || c1 == '{' ||
          c1 == '}')
      { res = res + '\\' + c1; } 
      else 
      { res = res + c1; } 
    } 

    String fres = ""; 
    for (int i = 0; i < res.length() - 1; i++) 
    { char c1 = res.charAt(i); 
      char c2 = res.charAt(i+1); 
      if (c1 == '_' && 
          (c2 == '1' || c2 == '2' || c2 == '3' || c2 == '4' 
           || c2 == '5' || c2 == '6' || c2 == '7' ||
           c2 == '8' || c2 == '9'))
      { fres = fres + "(.+)"; 
        i++; 
      } 
      else 
      { fres = fres + c1; } 
    } 
    return fres + res.charAt(res.length()-1); 
  } 

  public static void main(String[] args) 
  { // System.out.println(metafeatures("for (_1`elementType _2 : _1) do { _3 }")); 
    Vector vars = new Vector(); 
    vars.add("_1");
	vars.add("_2");
    CGRule r = new CGRule("createByPK_1(_2)", "_1.createByPK_1(_2)", vars, new Vector());
    String rr = r.applyTextRule("createByPKB(x)");
    System.out.println(rr);  
	
	Vector patts = convertToPatterns("createByPK_1(_2)");
	r.lhspatternlist = patts; 
	Vector matched = new Vector(); 
	boolean b = r.checkPatternList("createByPKB(x)", matched);
	System.out.println(b);   
  } 
}
