/**
 * 
 */
package com.autoStock.trading.platform.ib.tws;

import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JPasswordFieldOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

/**
 * @author Kevin Kowalewski
 * 
 */
public class TWSSupervisor {
	public void launchTws() {
		try {
			String[] params = new String[1];
			params[0] = "~/IBJts";
			new ClassReference("jclient.LoginFrame").startApplication(params);

			JFrameOperator loginFrame = new JFrameOperator("Login");
			JTextFieldOperator userNameField = new org.netbeans.jemmy.operators.JTextFieldOperator(loginFrame);
			JPasswordFieldOperator passwordField = new org.netbeans.jemmy.operators.JPasswordFieldOperator(loginFrame);

			JButtonOperator loginButton = new JButtonOperator(loginFrame, "Login");

			loginFrame.requestFocus();
			userNameField.requestFocus();
			userNameField.typeText("MYUSERNAME");
			passwordField.requestFocus();
			passwordField.typeText("MYPASSWORD");
			loginButton.requestFocus();
			loginButton.push();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}