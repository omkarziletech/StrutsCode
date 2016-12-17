package com.gp.cong.logisoft.bc.voyagemanagement;

import java.util.List;

import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;

public class TerminalBC {
	RefTerminalDAO refTerminalDAO = new RefTerminalDAO();

	RefTerminalTemp refTerminalTemp = new RefTerminalTemp();

	public RefTerminalTemp getTerminalName(String code, String codedesc) throws Exception {

		List terminalNumberList = refTerminalDAO.findForExport(code, codedesc);
		if (terminalNumberList != null && terminalNumberList.size() > 0) {
			refTerminalTemp = (RefTerminalTemp) terminalNumberList.get(0);
		}
		return refTerminalTemp;
	}

}
