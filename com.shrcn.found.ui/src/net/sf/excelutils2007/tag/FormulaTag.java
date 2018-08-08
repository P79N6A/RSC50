/*
 * Copyright 2003-2005 try2it.com.
 * Created on 2005-7-7
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package net.sf.excelutils2007.tag;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import net.sf.excelutils2007.ExcelParser;

/**
 * <p>
 * <b>FormulaTag</b> is a class which can output excel formula
 * </p>
 * @author rainsoft
 * @version $Revision: 1.1 $ $Date: 2011/07/19 06:15:21 $
 */
public class FormulaTag implements ITag {
  
  public static final String KEY_FORMULA = "#formula";
  
  public int[] parseTag(Object context, XSSFSheet sheet, XSSFRow curRow, XSSFCell curCell) {
 
    String cellstr = curCell.getStringCellValue();
    if (null == cellstr || "".equals(cellstr)) {
      return new int[] { 0, 0, 0 };
    }
    
    cellstr = cellstr.substring(KEY_FORMULA.length()).trim();
    
    Object formula = ExcelParser.parseStr(context, cellstr);
    
    if (null != formula) {
      curCell.setCellFormula(formula.toString());
    }
    
    return new int[]{0, 0, 0};
  }

  public boolean hasEndTag() {
    return false;
  }

  public String getTagName() {    
    return KEY_FORMULA;
  }

}
