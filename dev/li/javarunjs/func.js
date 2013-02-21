/*
 * java调用js方法示例
 */

/*
 * 一个测试的方法
 */
function sum(a, b) {
	return a + b;
}

function hello(name) {
	return "hello 你好 , " + name;
}

function alert(msg) {
	importPackage(javax.swing);
	var optionPane = JOptionPane.showMessageDialog(null, msg);
	return optionPane;
}

function confirm(title, msg) {
	importPackage(javax.swing);
	return JOptionPane.showConfirmDialog(null, msg, title,
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
}

function input(msg) {
	importPackage(javax.swing);
	return JOptionPane.showInputDialog(msg);
}

function dialog() {

}

function test() {
	alert(sum(123, 234) + hello("英雄"));
}