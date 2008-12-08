/*
 * NodeOptions_Beans.java
 *
 * Created on December 8, 2008, 2:03 AM
 */

package gui.beans;

/**
 * 
 * @author Jason
 */
public abstract class NodeOptions_Beans extends javax.swing.JPanel {

	/** Creates new form NodeOptions_Beans */
	public NodeOptions_Beans() {
		initComponents();
	}

	protected abstract void updateTitle(String newTitle);

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		titleField = new javax.swing.JTextField();

		jLabel1.setText("Title: ");

		titleField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				titleFieldActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addContainerGap().addComponent(
						jLabel1).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(titleField,
								javax.swing.GroupLayout.DEFAULT_SIZE, 349,
								Short.MAX_VALUE).addContainerGap()));
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
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
	}// </editor-fold>

	private void titleFieldActionPerformed(java.awt.event.ActionEvent evt) {
		if (titleField.getText().length() > 0)
			updateTitle(titleField.getText());
	}

	// Variables declaration - do not modify
	protected javax.swing.JLabel jLabel1;
	protected javax.swing.JTextField titleField;
	// End of variables declaration

}
