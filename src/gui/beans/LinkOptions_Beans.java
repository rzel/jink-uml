/*
 * LinkOptions_Beans.java
 *
 * Created on December 8, 2008, 11:01 PM
 */

package gui.beans;

/**
 * 
 * @author Jason
 */
public abstract class LinkOptions_Beans extends javax.swing.JPanel {

	/** Creates new form LinkOptions_Beans */
	public LinkOptions_Beans() {
		initComponents();
	}

	protected abstract void updateTitle(String newTitle);

	protected abstract void updateURL(String url);

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		titleField = new javax.swing.JTextField();
		urlField = new javax.swing.JTextField();

		jLabel1.setText("Title: ");

		jLabel2.setText("URL: ");

		titleField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				titleFieldActionPerformed(evt);
			}
		});
		titleField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusLost(java.awt.event.FocusEvent evt) {
				titleFieldFocusLost(evt);
			}
		});

		urlField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				urlFieldActionPerformed(evt);
			}
		});
		urlField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusLost(java.awt.event.FocusEvent evt) {
				urlFieldFocusLost(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel1)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				titleField,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				140,
																				Short.MAX_VALUE))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel2)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				urlField,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				141,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel1)
														.addComponent(
																titleField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel2)
														.addComponent(
																urlField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(330, Short.MAX_VALUE)));
	}// </editor-fold>

	private void titleFieldActionPerformed(java.awt.event.ActionEvent evt) {
		String txt = titleField.getText();
		if (txt.length() > 0) {
			updateTitle(txt);
		}
	}

	private void urlFieldActionPerformed(java.awt.event.ActionEvent evt) {
		String txt = urlField.getText();
		if (txt.length() > 0) {
			updateURL(txt);
		}
	}

	private void titleFieldFocusLost(java.awt.event.FocusEvent evt) {
		String txt = titleField.getText();
		if (txt.length() > 0) {
			updateTitle(txt);
		}
	}

	private void urlFieldFocusLost(java.awt.event.FocusEvent evt) {
		String txt = urlField.getText();
		if (txt.length() > 0) {
			updateURL(txt);
		}
	}

	// Variables declaration - do not modify
	protected javax.swing.JLabel jLabel1;
	protected javax.swing.JLabel jLabel2;
	protected javax.swing.JTextField titleField;
	protected javax.swing.JTextField urlField;
	// End of variables declaration

}
