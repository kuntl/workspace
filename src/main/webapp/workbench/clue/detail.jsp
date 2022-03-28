<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":"
            + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            $(".remarkDiv").mouseover(function () {
                $(this).children("div").children("div").show();
            });

            $(".remarkDiv").mouseout(function () {
                $(this).children("div").children("div").hide();
            });

            $(".myHref").mouseover(function () {
                $(this).children("span").css("color", "red");
            });

            $(".myHref").mouseout(function () {
                $(this).children("span").css("color", "#E6E6E6");
            });
            showRemarkList();
            showActivityAndClueRelationList(1,2);
            //
            $("#remarkBody").on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            })
            $("#remarkBody").on("mouseout", ".remarkDiv", function () {
                $(this).children("div").children("div").hide();
            })

            $("#updateRemarkBtn").click(function () {
                var id = $("#remarkId").val();
                $.ajax({
                    url: "workbench/clue/updateRemark.do",
                    data: {
                        "id": id,
                        "noteContent": $.trim($("#noteContent").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            //更新div中相应的信息，noteContent，editTime,editBy
                            $("#e" + id).html(data.cr.noteContent);
                            $("#s" + id).html(data.cr.editTime + " 由" + data.cr.editBy);
                            //更新内容之后，关闭模态窗口
                            $("#editRemarkModal").modal("hide");
                        } else {
                            alert("修改备注失败")
                        }
                    }
                })
            })

            $("#saveRemarkBtn").click(function () {
                $.ajax({
                    url: "workbench/clue/saveRemark.do",
                    data: {
                        "noteContent": $.trim($("#remark").val()),
                        "clueId": "${c.id}"
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            //将文本域中的信息清空
                            $("#remark").val("");
                            //添加成功之后，在textarea文本域上方新增一个div

                            var html = "";

                            html += '<div id="' + data.cr.id + '" class="remarkDiv" style="height: 60px;">';
                            html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                            html += '<div style="position: relative; top: -40px; left: 40px;" >';
                            html += '<h5 id="e' + data.cr.id + '">' + data.cr.noteContent + '</h5>';
                            html += '<font color="gray">线索</font> <font color="gray">-</font> <b>${c.fullname}${c.appellation}-${c.company}</b> <small style="color: gray;" id="s' + data.cr.id + '"> ' + (data.cr.createTime) + ' 由' + (data.cr.createBy) + '</small>';
                            html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                            html += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\'' + data.cr.id + '\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
                            html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                            html += '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\'' + data.cr.id + '\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                            html += '</div>';
                            html += '</div>';
                            html += '</div>';

                            $("#remarkDiv").before(html);

                        } else {
                            alert("添加备注失败");
                        }
                    }
                })
            })

            $("#bundBtn").click(function () {
                var pageNo = 1;
                var pageSize = 2;
                bundBtnOnClick(pageNo,pageSize);
            })

            $("#bundSave").click(function () {
                //找到复选框中 选中的复选框的jquery对象
                var $xz = $("input[name=xz]:checked");
                if ($xz.length == 0) {
                    alert("请选择需要关联的市场活动");
                } else {
                    //url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
                    //拼接参数
                    //alert("hello");
                    var param = "cid=${c.id}&";

                    for (var i = 0; i < $xz.length; i++) {
                        param += "aid=" + $($xz[i]).val();
                        if (i < $xz.length - 1) {
                            param += "&";
                        }
                    }
                    //alert(param);
                    $.ajax({
                        url: "workbench/clue/bundSave.do",
                        data: param,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.success) {
                                for (var i = 0; i < $xz.length; i++) {
                                    $("#car"+$($xz[i]).val()).remove();
                                }
                                //$("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
                                showActivityAndClueRelationList($("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                                $("#bundModal").modal("hide");
                            } else {
                                alert("关联市场活动失败")
                            }
                        }
                    })

                }
            })

            //为全选的复选框绑定事件，触发全选操作
            $("#qx").click(function () {
                //标签选择器
                $("input[name=xz]").prop("checked", this.checked);
            })
            //动态生成的元素是不能够以普通绑定事件的形式操作
            $("#bundActivityBody").on("click", $("input[name=xz]"), function () {
                $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length);
            })

            $("#aname").keydown(function (event) {
                if (event.keyCode == 13){
                    bundBtnOnClick(1,2);
                    return false;
                }
            })

        });

        function bundBtnOnClick(pageNo,pageSize){
            $.ajax({
                url: "workbench/clue/bundRelation.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "clueId": "${c.id}",
                    "a":$.trim($("#aname").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    $("#qx").prop("checked",false);
                    $("#bundActivityBody").html("");
                    var html = "";
                    $.each(data.dataList, function (i, n) {
                        html += '<tr id="car'+n.id+'">';
                        html += '<td><input type="checkbox" name="xz" value="' + n.id + '"/></td>';
                        html += '<td>' + n.name + '</td>';
                        html += '<td>' + n.startDate + '</td>';
                        html += '<td>' + n.endDate + '</td>';
                        html += '<td>' + n.owner + '</td>';
                        html += '</tr>';
                    })
                    $("#bundActivityBody").html(html);
                    $("#bundModal").modal("show");

                    //计算总页数
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;

                    //数据处理完毕后，结合分页查询，对前段展现分页信息
                    $("#activitybundPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        //该回调函数是在点击分页组件的时候触发的
                        onChangePage: function (event, data) {
                            bundBtnOnClick(data.currentPage, data.rowsPerPage);
                        }
                    });

                }
            })
        }

        function showActivityAndClueRelationList(pageNo,pageSize) {
            $.ajax({
                url: "workbench/clue/showActivityAndClueRelationList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "clueId": "${c.id}"
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    /*
                    data:{[a1,a2,...,a3]}
                     */
                    $("#relationBody").html("");
                    var html = "";
                    $.each(data.dataList, function (i, n) {
                        html += '<tr id="' + n.id + '">';
                        html += '<td>' + n.name + '</td>';
                        html += '<td>' + n.startDate + '</td>';
                        html += '<td>' + n.endDate + '</td>';
                        html += '<td>' + n.owner + '</td>';
                        html += '<td><a href="javascript:void(0);" style="text-decoration: none;" onclick="deleteRelation(\'' + n.id + '\')"><span';
                        html += 'class="glyphicon glyphicon-remove"></span>解除关联</a></td>';
                        html += '</tr>';
                    })
                    $("#relationBody").html(html);

                    //计算总页数
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;

                    //数据处理完毕后，结合分页查询，对前段展现分页信息
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        //该回调函数是在点击分页组件的时候触发的
                        onChangePage: function (event, data) {
                            showActivityAndClueRelationList(data.currentPage, data.rowsPerPage);
                        }
                    });
                }
            })
        }

        function deleteRelation(carId) {
            $.ajax({
                url: "workbench/clue/deleteRelation.do",
                data: {
                    "carId": carId
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        $("#" + carId).remove();
                        showActivityAndClueRelationList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        // $("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
                    } else {
                        alert("删除关联市场失败")
                    }
                }
            })
        }

        function showRemarkList() {

            $.ajax({
                url: "workbench/clue/getRemarkListByCid.do",
                data: {
                    "clueId": "${c.id}"
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    var html = "";
                    $.each(data, function (i, n) {
                        html += '<div id="' + n.id + '" class="remarkDiv" style="height: 60px;">';
                        html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                        html += '<div style="position: relative; top: -40px; left: 40px;" >';
                        html += '<h5 id="e' + n.id + '">' + n.noteContent + '</h5>';
                        html += '<font color="gray">线索</font> <font color="gray">-</font> <b>${c.fullname}${c.appellation}-${c.company}</b> <small style="color: gray;" id="s' + n.id + '"> ' + (n.editFlag == 0 ? n.createTime : n.editTime) + ' 由' + (n.editFlag == 0 ? n.createBy : n.editBy) + '</small>';
                        html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                        html += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\'' + n.id + '\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
                        html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                        html += '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\'' + n.id + '\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                        html += '</div>';
                        html += '</div>';
                        html += '</div>';
                    })
                    $("#remarkDiv").before(html);

                }
            })

        }

        function editRemark(id) {
            //找到指定的存放备注信息的h5标签
            var noteContent = $("#e" + id).html();
            //将展现出来的信息赋予到修改模态操作窗口的文本域中
            $("#remarkId").val(id);
            $("#noteContent").val(noteContent);
            $("#editRemarkModal").modal("show");
        }

        function deleteRemark(id) {
            $.ajax({
                url: "workbench/clue/deleteRemark.do",
                data: {
                    "id": id
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        //showRemarkList();
                        //找到需要删除记录的div,将div移除
                        $("#" + id).remove();
                    } else {
                        alert("删除备注失败")
                    }
                }
            })
        }

    </script>

</head>
<body>

<!-- 修改线索活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="remarkId">
    <div class="modal-dialog" role="document" style="width: 40%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改备注</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 关联市场活动的模态窗口 -->
<div class="modal fade" id="bundModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">关联市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;" id="aname"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td><input type="checkbox" id="qx"/></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody id="bundActivityBody">

                    </tbody>
                </table>
            </div>
            <div style="height: 30px; position: relative;top: 10px;">
                <div id="activitybundPage"></div>
            </div>
            <br>
            <br>
            <br>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" id="bundSave">关联</button>
            </div>
        </div>
    </div>
</div>


<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>${c.fullname}${c.appellation}<small>${c.company}</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" onclick="window.location.href='workbench/clue/convert.jsp';"><span
                class="glyphicon glyphicon-retweet"></span> 转换
        </button>
        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editClueModal"><span
                class="glyphicon glyphicon-edit"></span> 编辑
        </button>
        <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.fullname}${c.appellation}</b>
        </div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.owner}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">公司</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.company}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.job}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">邮箱</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.email}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.phone}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">公司网站</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.website}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.mphone}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">线索状态</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.state}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${c.source}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${c.createBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${c.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 60px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${c.editBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${c.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 70px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${c.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 80px;">
        <div style="width: 300px; color: gray;">联系纪要</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${c.contactSummary}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 90px;">
        <div style="width: 300px; color: gray;">下次联系时间</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${c.nextContactTime}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 100px;">
        <div style="width: 300px; color: gray;">详细地址</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${c.address}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div id="remarkBody" style="position: relative; top: 40px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>
    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
            </p>
        </form>
    </div>
</div>

<!-- 市场活动 -->
<div>
    <div style="position: relative; top: 60px; left: 40px;">
        <div class="page-header">
            <h4>市场活动</h4>
        </div>
        <div style="position: relative;top: 0px;">
            <table class="table table-hover" style="width: 900px;">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>名称</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                    <td>所有者</td>
                    <td></td>
                </tr>
                </thead>
                <tbody id="relationBody">
                </tbody>
            </table>
        </div>
        <div style="height: 50px; position: relative;top: 10px;">
            <div id="activityPage"></div>
        </div>
        <br>
        <br>
        <div>
            <a href="javascript:void(0);" id="bundBtn"
               style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
        </div>
    </div>
</div>


<div style="height: 200px;"></div>
</body>
</html>