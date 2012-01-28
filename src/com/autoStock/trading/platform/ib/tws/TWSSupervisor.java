/**
 * 
 */
package com.autoStock.trading.platform.ib.tws;

import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JPasswordFieldOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

import com.autoStock.internal.Config;

/**
 * @author Kevin Kowalewski
 * 
 */
public class TWSSupervisor {
	public void launchTws() {
		try {
			String[] params = new String[1];
			params[0] = "/autoStock/external.trading.ib.tws/IBjts";
			new ClassReference("jclient.LoginFrame").startApplication(params);

			JFrameOperator loginFrame = new JFrameOperator("Login");
			JTextFieldOperator userNameField = new org.netbeans.jemmy.operators.JTextFieldOperator(loginFrame);
			JPasswordFieldOperator passwordField = new org.netbeans.jemmy.operators.JPasswordFieldOperator(loginFrame);

			JButtonOperator loginButton = new JButtonOperator(loginFrame, "Login");

			loginFrame.requestFocus();
			userNameField.requestFocus();
			userNameField.typeText(Config.plIbUsername);
			passwordField.requestFocus();
			passwordField.typeText(Config.plIbPassword);
			loginButton.requestFocus();
			loginButton.push();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}