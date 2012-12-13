<#--
自定义指令genPagin的参数说明，如下
url           必填。用于指定链接所跳转的URL
totalPages    必填。用于指定总页数
currPage      选填。用于指定当前显示第几页，默认显示第一页
showPageNum   选填。用于指定所显示页链接的数量，默认显示10个页链接。否则总不能把所有页的链接都显示出来吧
class         选填。用于指定<a/>的class属性
-->
<#macro show url totalPages totalRecords currPage=1 pageSize=20 showPageNum=10  class="">
    <#local currPage = currPage?number/>
    <#local showPageNum = showPageNum?number/>
    <#local pageSize = pageSize?number/>
    <#local totalPages = totalPages?number/>
    <#local halfPage = (showPageNum/2)?number/>

${currPage} / ${totalPages} 页
    <#if ( currPage > 1) >
    <a href="javascript:getPage('${url}','${(currPage-2)*pageSize}')"><b>上一页</b></a>&nbsp;&nbsp;
        <#else >
        上一页
    </#if>

    <#if ( currPage < totalPages) >
    <a href="javascript:getPage('${url}','${(currPage)*pageSize}')"><b>下一页</b></a>&nbsp;&nbsp;
        <#else >
        下一页
    </#if>

共有记录${totalRecords} 条,到第
<input id="pageNum" name="pageNum" type="text" class="input-small" value="${currPage}"/>
页
<input id="gobutton" type="button" class="btn btn-primary" style="position: relative; left: 0px;  top: -5px;"
       value="GO.." onclick="javascript:checkGo(this,'${url}')"/>

<script type="text/javascript">
    function checkGo(obj, url) {
        var pageNo = $(obj).prev().attr('value');
        var preObj = $(obj).prev();
        if (isNaN(pageNo)) {
            alert('请输入数字!');
            preObj.focus();
            return false;
        } else if (pageNo == null || pageNo == '') {
            alert('请先输入页码!');
            preObj.focus();
            return false;
        } else if (pageNo < 1 || pageNo > ${totalPages}) {
            alert('输入的值超出页码范围!');
            preObj.focus();
            return false;
        } else {
            getPage(url, (pageNo - 1) *${pageSize} );
        }
    }

    var setVal = function(obj) {
        var pageNum = $(obj).val();
        $("#pageNum").val(pageNum);
    }

    var ua = window.navigator.userAgent;
    if (ua.indexOf('Firefox') >= 1) {
        document.onkeydown = function(event) {
            e = event ? event : (window.event ? window.event : null);
            if (e.keyCode == 13) {
                checkGo($("#gobutton"), '${url}')
            }
        }
    }
</script>
</#macro>

<#macro test_page>
	test page
</#macro>