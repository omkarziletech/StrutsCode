function getOnlineUsers(){
    document.onlineUsersForm.buttonValue.value="showOnlineUsers";
    document.onlineUsersForm.submit();

}
function getSystemTaskProcess(){
    document.onlineUsersForm.buttonValue.value="systemTaskProcess";
    document.onlineUsersForm.submit();

}
function removeOnlineUsers(userId){
    document.onlineUsersForm.userId.value=userId;
    document.onlineUsersForm.buttonValue.value="removeOnlineUsers";
    document.onlineUsersForm.submit();

}