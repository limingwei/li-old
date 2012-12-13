<#-- 自定义标签 li 20121213 -->

<#macro input name="" type="text" label="" value="" class="input-xlarge" maxlength="" readonly="" disabled="">
<div class="control-group">
    <label class="control-label " for="${name}">${label}</label>

    <div class="controls">
        <input type="${type}" class="${class}" id="${name}" name="${name}" maxlength="${maxlength}" value="${value}"
               <#if readonly!="">readonly</#if> <#if disabled!="">disabled</#if>/>
    </div>
</div>
</#macro>

<#macro select list key val name="" label="" value="" def="" disabled="">
<div id="${name}" class="control-group">
    <label class="control-label" for="${name}">${label}</label>

    <div class="controls">
        <select id="${name}" name="${name}" <#if disabled!="">disabled</#if>>
            <#if def!="">
                <option>${def}</option>
            </#if>
            <#list list as each>
                <option value="${each[key]}" <#if each[key]?string==value?string>selected</#if>>${each[val]}</option>
            </#list>
        </select>
    </div>
</div>
</#macro>

<#macro time name="" type="text" label="" value="" class="input-xlarge" fmt="yyyy-MM-dd HH:mm:ss">
<div class="control-group">
    <label class="control-label " for="${name}">${label}</label>

    <div class="controls">
        <input type="${type}" class="${class}" id="${name}" name="${name}" value="${value}"
               onclick="WdatePicker({dateFmt:'${fmt}'})"/>
    </div>
</div>
</#macro>

<#macro textarea name="" type="text" label="" value="" class="input-xlarge" cols="500" rows="10">
<div class="control-group">
    <label class="control-label " for="${name}">${label}</label>

    <div class="controls">
        <textarea cols="${cols}" rows="${rows}" class="${class}" id="${name}" name="${name}">${value}</textarea>
    </div>
</div>
</#macro>

<#-- <@li.pager url="/edm_user/list_page?start="/> -->
<#macro page url>
<@li.basic_page url="${rc.getContextPath()}"+url totalPages="${pager.totalPageCount}" totalRecords="${pager.totalCount}" currPage="${pager.currentPageNo}" pageSize="${pager.pageSize}"/>
</#macro>

<#-- 从page.ftl复制的,不要用 -->
<#macro basic_page url totalPages totalRecords currPage=1 pageSize=20 showPageNum=10  class="">
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

<#macro test>
	macro test ~!@#$%^&*()_+
</#macro>