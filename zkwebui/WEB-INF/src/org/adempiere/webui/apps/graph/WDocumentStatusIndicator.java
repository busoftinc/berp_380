/******************************************************************************
 * Copyright (C) 2008 Low Heng Sin                                            *
 * Copyright (C) 2008 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.webui.apps.graph;

import org.adempiere.model.IDocumentStatus;
import org.adempiere.model.MDocumentStatus;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.component.ZkCssHelper;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.MQuery;
import org.compiere.print.MPrintColor;
import org.compiere.print.MPrintFont;
import org.compiere.util.Env;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

/**
 * 	Performance Indicator
 *
 *  @author hengsin
 */
public class WDocumentStatusIndicator extends Panel implements EventListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 3580494126343850939L;

	/**
	 * 	Constructor
	 *	@param goal goal model
	 */
	public WDocumentStatusIndicator(IDocumentStatus documentStatus)
	{
		super();

		m_documentStatus = documentStatus;

		init();
		this.setStyle("border:1px solid #B1CBD5;");
		
		refresh();
		updateUI();
	}	//	PerformanceIndicator

	private IDocumentStatus		m_documentStatus = null;
	private int statusCount;
	private Label statusLabel;

	/**
	 * 	Get Document Status
	 *	@return Document Status
	 */
	public IDocumentStatus getDocumentStatus()
	{
		return m_documentStatus;
	}	//	getGoal

     /**
	 * 	Init Graph Display
	 *  Kinamo (pelgrim)
	 */
	private void init()
	{
		Label nameLabel = new Label();
		nameLabel.setText(m_documentStatus.getName());
		int AD_PrintColor_ID = m_documentStatus.getName_PrintColor_ID();
		MPrintColor printColor = MPrintColor.get(Env.getCtx(), AD_PrintColor_ID);
		String color = ZkCssHelper.createHexColorString(printColor.getColor());
		int AD_PrintFont_ID = m_documentStatus.getName_PrintFont_ID();
		MPrintFont printFont = MPrintFont.get(AD_PrintFont_ID);
		String family = printFont.getFont().getFamily();
		boolean bold = printFont.getFont().isBold();
		boolean italic = printFont.getFont().isItalic();
		int pointSize = printFont.getFont().getSize();
		String fontStyle = "font-family:'"+family+"';font-weight:"+(bold ? "bold" : "normal")+";font-style:"+(italic ? "italic" : "normal")+";font-size:"+pointSize+"pt";
		nameLabel.setStyle("color:#"+color+";"+fontStyle);

		statusLabel = new Label();
		AD_PrintColor_ID = m_documentStatus.getNumber_PrintColor_ID();
		printColor = MPrintColor.get(Env.getCtx(), AD_PrintColor_ID);
		color = ZkCssHelper.createHexColorString(printColor.getColor());
		AD_PrintFont_ID = m_documentStatus.getNumber_PrintFont_ID();
		printFont = MPrintFont.get(AD_PrintFont_ID);
		family = printFont.getFont().getFamily();
		bold = printFont.getFont().isBold();
		italic = printFont.getFont().isItalic();
		pointSize = printFont.getFont().getSize();
		fontStyle = "font-family:'"+family+"';font-weight:"+(bold ? "bold" : "normal")+";font-style:"+(italic ? "italic" : "normal")+";font-size:"+pointSize+"pt";
		int margin = pointSize;
		String marginStyle = "margin-top:"+margin+"pt;"+"margin-bottom:"+margin+"pt;";
		statusLabel.setStyle("color:#"+color+";"+fontStyle+";"+marginStyle);

		statusLabel.setWidth("60px");
		
		Hbox box = new Hbox();
		box.setZclass("docStatus");
		box.appendChild(statusLabel);
		box.appendChild(nameLabel);
		appendChild(box);

		this.addEventListener(Events.ON_CLICK, this);
	}


	public void onEvent(Event event) throws Exception
	{
		int AD_Window_ID = m_documentStatus.getAD_Window_ID();
		int AD_Form_ID = m_documentStatus.getAD_Form_ID();
		if (AD_Window_ID > 0)
		{
			MQuery query = new MQuery(m_documentStatus.getAD_Table_ID());
			query.addRestriction(MDocumentStatus.getWhereClause(m_documentStatus));
			AEnv.zoom(AD_Window_ID, query);
		}
		else if ( AD_Form_ID > 0 )
		{
			ADForm form = ADForm.openForm(AD_Form_ID);

			form.setAttribute(Window.MODE_KEY, Window.MODE_EMBEDDED);
			SessionManager.getAppDesktop().showWindow(form);
		}
		
	}

	public void refresh() {
		statusCount = MDocumentStatus.evaluate(m_documentStatus);		
	}

	public void updateUI() {
		statusLabel.setText(Integer.toString(statusCount));		
	}
}
