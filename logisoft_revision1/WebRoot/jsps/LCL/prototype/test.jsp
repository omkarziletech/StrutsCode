<%-- 
    Document   : test
    Created on : Nov 3, 2011, 10:41:31 AM
    Author     : Thamizh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/2000/REC-xhtml1-200000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="title" content="Subscription Signup | Marketo" />
        <meta name="robots" content="index, follow" />
        <meta name="description" content="Marketo Search Marketing application" />
        <meta name="keywords" content="Marketo, Search Marketing" />
        <meta name="language" content="en" />
        <title>Subscription Signup | Marketo</title>

        <link rel="shortcut icon" href="/favicon.ico" />

        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/validation/jquery.validate.js"></script>
        <script type="text/javascript" src="js/validation/jquery.maskedinput.js"></script>
        <script type="text/javascript" src="js/validation/mktSignup.js"></script>

        <link rel="stylesheet" type="text/css" media="screen" href="css/validation/stylesheet.css" />
    </head>
    <body>

        <div id="letterbox">
            <div id="col-main" class="resize" style="">

                <form id="profileForm" type="actionForm" action="step2.htm" method="get" >


                    <div class="error" style="display:none;">
                        <img src="images/warning.gif" alt="Warning!" width="24" height="24" style="float:left; margin: -5px 10px 0px 0px; " />

                        <span></span>.<br clear="all"/>
                    </div>


                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td class="label"><label for="co_name">Company Name:</label></td>
                            <td class="field">
                                <input id="co_name" class="required" maxlength="40" name="co_name" size="20" type="text" tabindex="1" value="" />

                            </td>
                        </tr>
                        <tr>
                            <td class="label"><label for="co_url">Company URL:</label></td>
                            <td class="field">
                                <input id="co_url" class="required defaultInvalid url" maxlength="40" name="co_url" style="width:163px" type="text" tabindex="2" value="http://" />
                            </td>
                        </tr>

                        <tr>
                            <td/><td/>
                        </tr>
                        <tr>
                            <td class="label"><label for="first_name">First Name:</label></td>
                            <td class="field">
                                <input id="first_name" class="required" maxlength="40" name="first_name" size="20" type="text" tabindex="3" value="" />
                            </td>

                        </tr>
                        <tr>
                            <td class="label"><label for="last_name">Last Name:</label></td>
                            <td class="field">
                                <input id="last_name" class="required" maxlength="40" name="last_name" size="20" type="text" tabindex="4" value="" />
                            </td>
                        </tr>
                        <tr>

                            <td class="label"><label for="address1">Company Address:</label></td>
                            <td class="field">
                                <input  maxlength="40" class="required" name="address1" size="20" type="text" tabindex="5" value="" />
                            </td>
                        </tr>
                        <tr>
                            <td class="label"></td>
                            <td class="field">

                                <input  maxlength="40" name="address2" size="20" type="text" tabindex="6" value="" />
                            </td>
                        </tr>
                        <tr>
                            <td class="label"><label for="city">City:</label></td>
                            <td class="field">
                                <input  maxlength="40" class="required" name="city" size="20" type="text" tabindex="7" value="" />
                            </td>

                        </tr>
                        <tr>
                            <td class="label"><label for="state">State:</label></td>
                            <td class="field">
                                <select id="state" class="required" name="state" style="margin-left: 4px;" tabindex="8">
                                    <option value="">Choose State:</option>
                                    <option value="AL">Alabama</option><option value="AK">Alaska</option><option value="AZ">Arizona</option><option value="AR">Arkansas</option><option value="CA">California</option><option value="CO">Colorado</option><option value="CT">Connecticut</option><option value="DE">Delaware</option><option value="FL">Florida</option><option value="GA">Georgia</option><option value="HI">Hawaii</option><option value="ID">Idaho</option><option value="IL">Illinois</option><option value="IN">Indiana</option><option value="IA">Iowa</option><option value="KS">Kansas</option><option value="KY">Kentucky</option><option value="LA">Louisiana</option><option value="ME">Maine</option><option value="MD">Maryland</option><option value="MA">Massachusetts</option><option value="MI">Michigan</option><option value="MN">Minnesota</option><option value="MS">Mississippi</option><option value="MO">Missouri</option><option value="MT">Montana</option><option value="NE">Nebraska</option><option value="NV">Nevada</option><option value="NH">New Hampshire</option><option value="NJ">New Jersey</option><option value="NM">New Mexico</option><option value="NY">New York</option><option value="NC">North Carolina</option><option value="ND">North Dakota</option><option value="OH">Ohio</option><option value="OK">Oklahoma</option><option value="OR">Oregon</option><option value="PA">Pennsylvania</option><option value="RI">Rhode Island</option><option value="SC">South Carolina</option><option value="SD">South Dakota</option><option value="TN">Tennessee</option><option value="TX">Texas</option><option value="UT">Utah</option><option value="VT">Vermont</option><option value="VA">Virginia</option><option value="WA">Washington</option><option value="WV">West Virginia</option><option value="WI">Wisconsin</option><option value="WY">Wyoming</option>

                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td class="label"><label for="zip">Zip:</label></td>
                            <td class="field">
                                <input  maxlength="10" name="zip" style="width: 100px" type="text" class="required zipcode" tabindex="9" value="" />
                            </td>

                        </tr>
                        <tr>
                            <td class="label"><label for="phone">Phone:</label></td>
                            <td class="field">
                                <input id="phone" maxlength="14" name="phone" type="text" class="required phone" tabindex="10" value="" />
                            </td>
                        </tr>


                        <tr>
                            <td colspan="2">
                                <h2 style="border-bottom: 1px solid #CCCCCC;">Login Information</h2>
                            </td>
                        </tr>


                        <tr>
                            <td class="label"><label for="email">Email:</label></td>

                            <td class="field">
                                <input id="email" class="required email" remote="emails.php" maxlength="40" name="email" size="20" type="text" tabindex="11" value="" />
                            </td>
                        </tr>

                        <tr>
                            <td class="label"><label for="password1">Password:</label></td>
                            <td class="field">
                                <input id="password1" class="required password" maxlength="40" name="password1" size="20" type="password" tabindex="12" value="" />

                            </td>
                        </tr>
                        <tr>
                            <td class="label"><label for="password2">Retype Password:</label></td>
                            <td class="field">
                                <input id="password2" class="required" equalTo="#password1" maxlength="40" name="password2"  size="20" type="password" tabindex="13" value="" />
                                <div class="formError"></div>
                            </td>

                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <div class="buttonSubmit">
                                    <span></span>
                                    <input class="formButton" type="submit" value="Next" style="width: 140px" tabindex="14" />
                                </div>

                            </td>
                        </tr>
                    </table><br /><br />
                </form>
                <br clear="all"/>
            </div>
        </div> <!-- end col-main -->
    </body>
</html>

