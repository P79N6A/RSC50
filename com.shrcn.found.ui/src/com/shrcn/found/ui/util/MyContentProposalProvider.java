/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.util.ArrayList;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-12-7
 */
/**
 * $Log: MyContentProposalProvider.java,v $
 * Revision 1.1  2013/03/29 09:36:47  cchun
 * Add:创建
 *
 * Revision 1.2  2012/01/13 08:39:55  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.1  2011/12/07 09:26:21  cchun
 * Update:添加文本提示功能
 *
 */
public class MyContentProposalProvider implements IContentProposalProvider {

	/*
	 * The proposals provided.
	 */
	private String[] proposals;

	/*
	 * The proposals mapped to IContentProposal. Cached for speed in the case
	 * where filtering is not used.
	 */
	private IContentProposal[] contentProposals;

	/*
	 * Boolean that tracks whether filtering is used.
	 */
	private boolean filterProposals = false;

	/**
	 * Construct a SimpleContentProposalProvider whose content proposals are
	 * always the specified array of Objects.
	 * 
	 * @param proposals
	 *            the array of Strings to be returned whenever proposals are
	 *            requested.
	 */
	public MyContentProposalProvider(String[] proposals) {
		super();
		this.proposals = proposals;
	}

	/**
	 * Return an array of Objects representing the valid content proposals for a
	 * field. Ignore the current contents of the field.
	 * 
	 * @param contents
	 *            the current contents of the field (only consulted if filtering
	 *            is set to <code>true</code>)
	 * @param position
	 *            the current cursor position within the field (ignored)
	 * @return the array of Objects that represent valid proposals for the field
	 *         given its current content.
	 */
	public IContentProposal[] getProposals(String contents, int position) {
		if (filterProposals) {
			ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
			for (int i = 0; i < proposals.length; i++) {
				int contentLength = contents.length();
				if (proposals[i].length() > contentLength
						&& proposals[i].toLowerCase().indexOf(contents.toLowerCase()) > -1) {
//						&& proposals[i].substring(0, contentLength)
//								.equalsIgnoreCase(contents)) {
					list.add(makeContentProposal(proposals[i]));
				}
			}
			return (IContentProposal[]) list.toArray(new IContentProposal[list
					.size()]);
		}
		if (contentProposals == null) {
			contentProposals = new IContentProposal[proposals.length];
			for (int i = 0; i < proposals.length; i++) {
				contentProposals[i] = makeContentProposal(proposals[i]);
			}
		}
		return contentProposals;
	}

	/**
	 * Set the Strings to be used as content proposals.
	 * 
	 * @param items
	 *            the array of Strings to be used as proposals.
	 */
	public void setProposals(String[] items) {
		this.proposals = items;
		contentProposals = null;
	}

	/**
	 * Set the boolean that controls whether proposals are filtered according to
	 * the current field content.
	 * 
	 * @param filterProposals
	 *            <code>true</code> if the proposals should be filtered to
	 *            show only those that match the current contents of the field,
	 *            and <code>false</code> if the proposals should remain the
	 *            same, ignoring the field content.
	 * @since 3.3
	 */
	public void setFiltering(boolean filterProposals) {
		this.filterProposals = filterProposals;
		// Clear any cached proposals.
		contentProposals = null;
	}

	/*
	 * Make an IContentProposal for showing the specified String.
	 */
	private IContentProposal makeContentProposal(final String proposal) {
		return new IContentProposal() {
			public String getContent() {
				return proposal;
			}

			public String getDescription() {
				return null;
			}

			public String getLabel() {
				return null;
			}

			public int getCursorPosition() {
				return proposal.length();
			}
		};
	}
}
