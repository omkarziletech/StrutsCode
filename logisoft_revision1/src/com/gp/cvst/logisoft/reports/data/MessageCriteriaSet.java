package com.gp.cvst.logisoft.reports.data;

import java.util.List;

/**
 * use this class to transfer DTO's
 * as a data source to the Report Source
 * @author Rahul
 *
 */
public class MessageCriteriaSet extends CriteriaSet {

	
		private List messages;

		public List getMessages() {
			return messages;
		}

		public void setMessages(List messages) {
			this.messages = messages;
		}
}
