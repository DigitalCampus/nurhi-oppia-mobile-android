/* 
 * This file is part of OppiaMobile - https://digital-campus.org/
 * 
 * OppiaMobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OppiaMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OppiaMobile. If not, see <http://www.gnu.org/licenses/>.
 */

package org.digitalcampus.oppia.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.activity.OppiaMobileActivity;
import org.digitalcampus.oppia.activity.WelcomeActivity;
import org.digitalcampus.oppia.application.MobileLearning;
import org.digitalcampus.oppia.application.SessionManager;
import org.digitalcampus.oppia.application.Tracker;
import org.digitalcampus.oppia.gamification.GamificationEngine;
import org.digitalcampus.oppia.listener.SubmitListener;
import org.digitalcampus.oppia.model.User;
import org.digitalcampus.oppia.task.Payload;
import org.digitalcampus.oppia.task.RegisterTask;
import org.digitalcampus.oppia.utils.UIUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class RegisterFragment extends AppFragment implements SubmitListener, RegisterTask.RegisterListener {

	public static final String TAG = RegisterFragment.class.getSimpleName();


	private EditText usernameField;
	private EditText emailField;
	private EditText passwordField;
	private EditText passwordAgainField;
	private EditText firstnameField;
	private EditText lastnameField;
	private EditText jobTitleField;
	private EditText organisationField;
	private EditText phoneNoField;
	private Button registerButton;
	private Button loginButton;
	private ProgressDialog pDialog;
	
	public static RegisterFragment newInstance() {
	    return new RegisterFragment();
	}

	public RegisterFragment(){
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View vv = inflater.inflate(R.layout.fragment_register, container, false);
		usernameField = (EditText) vv.findViewById(R.id.register_form_username_field);
		emailField = (EditText) vv.findViewById(R.id.register_form_email_field);
		passwordField = (EditText) vv.findViewById(R.id.register_form_password_field);
		passwordAgainField = (EditText) vv.findViewById(R.id.register_form_password_again_field);
		firstnameField = (EditText) vv.findViewById(R.id.register_form_firstname_field);
		lastnameField = (EditText) vv.findViewById(R.id.register_form_lastname_field);
		jobTitleField = (EditText) vv.findViewById(R.id.register_form_jobtitle_field);
		organisationField = (EditText) vv.findViewById(R.id.register_form_organisation_field);
		phoneNoField = (EditText) vv.findViewById(R.id.register_form_phoneno_field);

        /*
		currentlyWorkingFacilityField = (EditText) vv.findViewById(R.id.currently_working_facility_field);

		nurhiTrainingField = (EditText) vv.findViewById(R.id.register_form_nurhi_training_field);

		staffTypeField = (Spinner) super.getActivity().findViewById(R.id.staff_type_spinner);
		ArrayAdapter<CharSequence> stadapter = ArrayAdapter.createFromResource(super.getActivity(),
				R.array.registerFormStaffType, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		stadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		staffTypeField.setAdapter(stadapter);

		fpMethodsProvided = (LinearLayout) super.getActivity().findViewById(R.id.register_form_fp_methods);
		fpMethodsProvided.removeAllViews();
		String[] fpMethods = getResources().getStringArray(R.array.registerFormFPMethods);
		for (String s: fpMethods){
			CheckBox chk= new CheckBox(super.getActivity());
			chk.setText(s);
			fpMethodsProvided.addView(chk);
		}

		highestEducationLevelField = (Spinner) super.getActivity().findViewById(R.id.education_level_spinner);
		ArrayAdapter<CharSequence> heladapter = ArrayAdapter.createFromResource(super.getActivity(),
				R.array.registerFormEducationalLevel, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		heladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		highestEducationLevelField.setAdapter(heladapter);

		religionField = (Spinner) super.getActivity().findViewById(R.id.religion_spinner);
		ArrayAdapter<CharSequence> radapter = ArrayAdapter.createFromResource(super.getActivity(),
				R.array.registerFormReligion, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		radapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		religionField.setAdapter(radapter);

		sexField = (Spinner) super.getActivity().findViewById(R.id.sex_spinner);
		ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(super.getActivity(),
				R.array.registerFormSex, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sexField.setAdapter(sadapter);

		ageField = (EditText) super.getActivity().findViewById(R.id.register_form_age_field);
		*/

		registerButton = (Button) vv.findViewById(R.id.register_btn);
		loginButton = (Button) vv.findViewById(R.id.login_btn);
		return vv;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onRegisterClick();
			}
		});
		loginButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				WelcomeActivity wa = (WelcomeActivity) RegisterFragment.super.getActivity();
				wa.switchTab(WelcomeActivity.TAB_LOGIN);
			}
		});
	}

	public void submitComplete(Payload response) {
		pDialog.dismiss();
		if (response.isResult()) {
			User user = (User) response.getData().get(0);
            SessionManager.loginUser(getActivity(), user);

			// registration gamification
			GamificationEngine gamificationEngine = new GamificationEngine(super.getActivity());
			gamificationEngine.processEventRegister();

            //Save the search tracker
            new Tracker(super.getActivity()).saveRegisterTracker();

	    	startActivity(new Intent(getActivity(), OppiaMobileActivity.class));
	    	super.getActivity().finish();
		} else {
			Context ctx = super.getActivity();
			if (ctx != null){
				try {
					JSONObject jo = new JSONObject(response.getResultResponse());
					UIUtils.showAlert(ctx,R.string.error,jo.getString("error"));
				} catch (JSONException je) {
					UIUtils.showAlert(ctx,R.string.error,response.getResultResponse());
				}
			}
		}
	}

	public void onRegisterClick() {
		// get form fields
		String username = usernameField.getText().toString().trim();
		String email = emailField.getText().toString();
		String password = passwordField.getText().toString();
		String passwordAgain = passwordAgainField.getText().toString();
		String firstname = firstnameField.getText().toString();
		String lastname = lastnameField.getText().toString();
		String phoneNo = phoneNoField.getText().toString();
		String jobTitle = jobTitleField.getText().toString();
		String organisation = organisationField.getText().toString();
		
		// do validation
		// check username
		if (username.length() == 0) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_username);
			return;
		}
			
		if (username.contains(" ")) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_username_spaces);
			return;
		}

		if (email.length() == 0) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_email);
			return;
		}
		
		// check password length
		if (password.length() < MobileLearning.PASSWORD_MIN_LENGTH) {
			UIUtils.showAlert(super.getActivity(),R.string.error,getString(R.string.error_register_password,  MobileLearning.PASSWORD_MIN_LENGTH ));
			return;
		}
		
		// check password match
		if (!password.equals(passwordAgain)) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_password_no_match);
			return;
		}
		
		// check firstname
		if (firstname.length() < 2) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_firstname);
			return;
		}

		// check lastname
		if (lastname.length() < 2) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_lastname);
			return;
		}

		// check phone no
		if (phoneNo.length() < 8) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_phoneno);
			return;
		}

    	User u = new User();
		u.setUsername(username);
		u.setPassword(password);
		u.setPasswordAgain(passwordAgain);
		u.setFirstname(firstname);
		u.setLastname(lastname);
		u.setEmail(email);
		u.setJobTitle(jobTitle);
		u.setOrganisation(organisation);
		u.setPhoneNo(phoneNo);

		executeRegisterTask(u);
	}

	@Override
	public void onSubmitComplete(User registeredUser) {
		pDialog.dismiss();
		SessionManager.loginUser(getActivity(), registeredUser);
		// registration gamification
		GamificationEngine gamificationEngine = new GamificationEngine(super.getActivity());
		gamificationEngine.processEventRegister();

		//Save the search tracker
		new Tracker(super.getActivity()).saveRegisterTracker();
		startActivity(new Intent(getActivity(), OppiaMobileActivity.class));
		super.getActivity().finish();
	}

	@Override
	public void onSubmitError(String error) {
		pDialog.dismiss();
		Context ctx = super.getActivity();
		if (ctx != null){
			UIUtils.showAlert(getActivity(), R.string.error, error);
		}
	}

	@Override
	public void onConnectionError(String error, final User u) {
		pDialog.dismiss();
		Context ctx = super.getActivity();
		if (ctx != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setCancelable(false);
			builder.setTitle(error);
			builder.setMessage(R.string.offline_register_confirm);
			builder.setPositiveButton(R.string.register_offline, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					u.setOfflineRegister(true);
					executeRegisterTask(u);
				}
			});
			builder.setNegativeButton(R.string.cancel, null);
			builder.show();
		}
	}

	private void executeRegisterTask(User u){

		pDialog = new ProgressDialog(super.getActivity());
		pDialog.setTitle(R.string.register_alert_title);
		pDialog.setMessage(getString(R.string.register_process));
		pDialog.setCancelable(true);
		pDialog.show();

		Payload p = new Payload(Arrays.asList(u));
		RegisterTask rt = new RegisterTask(super.getActivity());
		rt.setRegisterListener(this);
		rt.execute(p);
	}
}
