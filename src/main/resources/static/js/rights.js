/**
 * 权益管理
 * 
 * @author chaoxueling
 * @create 2017-03-18 9:14
 */

(function() {
	var memberRightsTool = {
		Config : {
			// add_url : "/member-rights/rights-add-save",
			// edit_url : "/member-rights/rights-edit-save",
			del_url : getUrmsPath() + "member-rights/rights-del",
			online_url : getUrmsPath() + "member-rights/rights-online",
			off_url : getUrmsPath() + "member-rights/rights-off"
		},
		ajax : function(params, suc, reqUrl, callback) {
			var that = this;
			$.ajax({
				type : "POST",
				cache : false,
				dataType : "json",
				url : reqUrl,
				data : params,
				success : arguments[1] || function() {
				},
				error : arguments[3] || function() {
					if (window.console) {
						console.log("error log: " + data.responseText);
					}
				}
			});
		},
		del_rights : function() {// 删除
			var that = this;
			var id = $("#txtDelId").val();
			if (id == "" || id == null) {
				alert("请先选择删除的权益");
				return;
			}
			var params = {
				"id" : id
			};
			var suc = function(data) {
				if (data.errorCode == "0") {
					/* alert(data.errorMsg); */
					window.location.href = getUrmsPath()
							+ 'member-rights/rights-list';
				} else {
					// alert(data.errorMsg);
					$('#row-type3').modal('hide');
					$('body').css({
						'overflow' : 'hidden'
					})
					$('#row-type4').modal('show');
					$('.upMessage').text(data.errorMsg);
				}
			};
			var err = function() {

			};
			that.ajax(params, suc, that.Config.del_url, err);
		},
		on_rights : function() {// 上架
			var id_array = new Array();
			var chk_list = $("input[name='chk_list']:checked");
			if (chk_list == null || chk_list == '') {
				/* alert("请选择要上架的权益!"); */
				$('.errMessage-1').show();
				return;
			}
			$(chk_list).each(function() {
				id_array.push($(this).val());// 向数组中添加元素
			});
			var that = this;
			var id_List = id_array.join(',');
			if (id_List == "" || id_List == null) {
				/* alert("请先选择要上架的权益"); */
				$('.errMessage-1').show();
				return;
			}
			var params = {
				"idList" : id_List
			};
			var suc = function(data) {
				if (data.errorCode == "0") {
					window.location.href = getUrmsPath()
							+ 'member-rights/rights-list';
				} else {
					$('#row-type1').modal('hide');
					$('body').css({
						'overflow' : 'hidden'
					})
					$('#row-type4').modal('show');
					$('.upMessage').text(data.errorMsg);
				}

			};
			var err = function() {
			};

			that.ajax(params, suc, that.Config.online_url, err);
		},
		off_rights : function() {// 下架
			var id_array = new Array();
			var chk_list = $("input[name='chk_list']:checked");
			if (chk_list == null || chk_list == '') {
				$('.errMessage-2').show();
				return;
			}
			$(chk_list).each(function() {
				id_array.push($(this).val());// 向数组中添加元素
			});
			var id_List = id_array.join(',');
			var that = this;
			if (id_List == "" || id_List == null) {
				$('.errMessage-2').show();
				return;
			}
			var params = {
				"idList" : id_List
			};
			var suc = function(data) {
				if (data.errorCode == "0") {
					window.location.href = getUrmsPath()
							+ 'member-rights/rights-list';
				} else {
					$('#row-type2').modal('hide');
					$('body').css({
						'overflow' : 'hidden'
					})
					$('#row-type4').modal('show');
					$('.upMessage').text(data.errorMsg);
				}
			};
			var err = function() {
			};
			that.ajax(params, suc, that.Config.off_url, err);
		},
		checkEndTime : function(startTimeId, endTimeId) {
			var startTime = $(startTimeId).val();
			var start = new Date(startTime.replace("-", "/").replace("-", "/"));
			var endTime = $(endTimeId).val();
			var end = new Date(endTime.replace("-", "/").replace("-", "/"));
			if (end < start) {
				return false;
			}
			return true;
		}
	};
	window.memberRightsTool = memberRightsTool;

})();

$(function() {

	$('#closeModalBtn').click(function() {
		$('body').css({
			'overflow' : 'auto'
		})
	})

	/* 全选 */
	$("#selectAll").click(function() {
		if (this.checked) {
			$(".checkList :checkbox").prop("checked", true);
		} else {
			$(".checkList :checkbox").prop("checked", false);
		}
	});
	/* 反选 */
	$(".checkList").click(function() {
		var allLength = $(".checkList input").length;
		var checkedLength = $(".checkList input:checked").length;
		$("#selectAll").prop("checked", allLength === checkedLength);
	});

	/* 权益新增-修改 */
	var d = new Date();
	var starTime = d.getFullYear() + "-" + (d.getMonth() + 1) + "-"
			+ d.getDate(), endTime = d.getFullYear() + "-" + (d.getMonth() + 1)
			+ "-" + d.getDate();
	$(".form-date").datepicker({
		format : 'yyyy-mm-dd',
		language : 'zh-CN',
		autoclose : true,
		startDate : starTime,
	});
	$('#form-query').bootstrapValidator();

	// $("#iptfileupload").change(function() {
    //
	// 	convertToImgBaseCharter(this, function(data) {
    //
	// 		$('#rightsImgPath').attr('src', data);
	// 	});
    //
	// })
});

/* 上下架 */
function online() { // 上架
	var checkedLength = $(".checkList input:checked").length;
	if (checkedLength == 0) {
		$('#row-type4').modal('show');
		$('.upMessage').text('请先勾选要上架的权益');
		return false
	}
	$('body').css({
		'overflow' : 'hidden'
	})
	$('.errMessage-1').hide();
	$('#row-type1').modal('show');
}
function off() { // 下架
	var checkedLength = $(".checkList input:checked").length;
	if (checkedLength == 0) {
		$('#row-type4').modal('show');
		$('.upMessage').text('请先勾选要下架的权益');
		return false
	}
	$('body').css({
		'overflow' : 'hidden'
	})
	$('.errMessage-2').hide();
	$('#row-type2').modal('show');
}
/*
 * 删除行
 */
function delRow(obj) {
	var id = $(obj).attr("rightsId");
	$("#txtDelId").val(id);
	$('#row-type3').modal('show');
	$('.errMessage-1').hide().text('');
}

/* 权益新增-修改 */
function chkDate() {
	$("#submitbtn").removeAttr("disabled");
}
// function convertToImgBaseCharter(input_file, get_data) {
//
// 	if (typeof (FileReader) === 'undefined') {
// 		$('#row-type').modal('show');
// 		$('.dateMessage').text('抱歉，你的浏览器不支持，请更换google浏览器');
// 		return;
// 	} else {
// 		try {
// 			/* 图片转Base64 核心代码 */
// 			var file = input_file.files[0];
// 			var size = file.size;
// 			var sizeKB = parseInt(size / 1024);
// 			if (sizeKB >= 3076) {
// 				$('#rightsImgPath').attr('src', getUrmsPath() + 'img/up.png');
// 				/* alert("上传图片不能大于5M"); */
// 				$('#row-type').modal('show');
// 				$('.dateMessage').text('上传的图片不能大于3M');
// 				return;
// 			}
// 			if (file.type != "image/png" && file.type != "image/jpg"
// 					&& file.type != "image/gif" && file.type != "image/jpeg") {
// 				$('#row-type').modal('show');
// 				$('.dateMessage').text('只支持png，jpg，jpeg，gif格式的图片,请重新选择上传');
// 				$('#rightsImgPath').attr('src', getUrmsPath() + 'img/up.png');
// 				return;
// 			}
// 			var reader = new FileReader();
// 			reader.onload = function() {
// 				get_data(this.result);
// 			}
// 			reader.readAsDataURL(file);
// 		} catch (e) {
// 			('#rightsImgPath').attr('src', getUrmsPath() + 'img/up.png');
// 			/* alert("上传图片失败，请重试!") */
// 			$('#row-type').modal('show');
// 			$('.dateMessage').text('上传图片失败，请重试！');
// 		}
// 	}
// }
function submitFun() {
	var isValid = $('#form-query').data('bootstrapValidator').isValid();
	if ($('#useStartTime').val() == '' || $('#useStartTime').val() == null) {

		if ($("#submitbtn").attr("disabled") == undefined
				|| $("#submitbtn").attr("disabled") == "undefined") {
			$("#submitbtn").attr("disabled", "disabled");
		}
		/* alert("请输入有效的开始日期!"); */
		$('#row-type').modal('show');
		$('.dateMessage').text('请输入有效的开始日期！');
		return false;
	}
	if ($('#useEndTime').val() == '' || $('#useEndTime').val() == null) {
		/*$('#useEndTime').focus();*/
		if ($("#submitbtn").attr("disabled") == undefined
				|| $("#submitbtn").attr("disabled") == "undefined") {
			$("#submitbtn").attr("disabled", "disabled");
		}
		/* alert("请输入有效的结束日期!"); */
		$('#row-type').modal('show');
		$('.dateMessage').text('请输入有效的结束日期！');
		return false;
	}
	var useStartTime = $('#useStartTime').val();
	var currentTime = getNowFormatDate();
	var start = new Date(useStartTime.replace("-", "/").replace("-", "/"));
	var end = new Date(currentTime.replace("-", "/").replace("-", "/"));
	if (start < end) {
		if ($("#submitbtn").attr("disabled") == undefined
				|| $("#submitbtn").attr("disabled") == "undefined") {
			$("#submitbtn").attr("disabled", "disabled");
		}
		/* alert("开始有效日期必须大于当前日期!"); */
		$('#row-type').modal('show');
		$('.dateMessage').text('开始有效日期必须大于当前日期！');
		return false;
	}
	if (!memberRightsTool.checkEndTime('#useStartTime', '#useEndTime')) {
		if ($("#submitbtn").attr("disabled") == undefined
				|| $("#submitbtn").attr("disabled") == "undefined") {
			$("#submitbtn").attr("disabled", "disabled");
		}
		/* alert("结束时间必须晚于开始时间！"); */
		$('#row-type').modal('show');
		$('.dateMessage').text('结束时间必须晚于开始时间！');
		return false;
	}
	if (isValid == false)
		return false;
	$('#row-type-save').modal('show');
	$('.dateMessage').text('权益保存成功！正在跳转。。。');
	return true;
}
