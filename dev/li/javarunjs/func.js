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
}

function test(){
	alert(sum(123,234)+hello("英雄"));
}