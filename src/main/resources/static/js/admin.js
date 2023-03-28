let userpageApi = "http://localhost:8080/userpage/view"
let categorypageApi = "http://localhost:8080/categorypage/view"
let productpageApi = "http://localhost:8080/productpage/view"
let invoicepageApi = "http://localhost:8080/invoicepage/view"
let invoicepageviewDetailApi = "http://localhost:8080/invoicepage/viewDetail"

function formatVND(n, currency) {
  return n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + currency;
}

function deleteUser(id, fullName) {
	$('#fullName').text(fullName);
	$('#yesOption').attr('href', '/userpage/delete?id=' + id);
	$('#modelDelete').modal('show');
}

function viewUser(id) {
	$("#modelView").modal('show');
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: userpageApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData)
			$("#viewid").text(responseData.id);
			$("#viewemail").text(responseData.email);
			$("#viewfullName").text(responseData.fullName);
			$("#viewaddress").text(responseData.address);
			$("#viewbirthDay").text(responseData.birthDay);
			$("#viewrole").text(responseData.role);
			$("#viewaccountType").text(responseData.accountType);
			$("#viewcreateDay").text(responseData.createDay);
			$("#viewupdateDate").text(responseData.updateDate);
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
}

function deleteCategory(id, name) {
	$('#name').text(name);
	$('#yesOption').attr('href', '/categorypage/delete?id=' + id);
	$('#modelDelete').modal('show');
}

function viewCategory(id) {
	$("#modelView").modal('show');
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: categorypageApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData)
			$("#viewid").text(responseData.id);
			$("#viewname").text(responseData.name);
			$("#viewcreateDay").text(responseData.createDay);
			$("#viewupdateDate").text(responseData.updateDate);
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
}

function readURLProductForm(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#blah').attr('src', e.target.result);
		};
		reader.readAsDataURL(input.files[0]);
	}
}

function deleteProduct(id, name) {
	$('#name').text(name);
	$('#yesOption').attr('href', '/productpage/delete?id=' + id);
	$('#modelDelete').modal('show');
}

function viewProduct(id) {
	$("#modelView").modal('show');
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: productpageApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData)
			$("#viewid").text(responseData.id);
			$("#viewname").text(responseData.name);
			$("#viewquantity").text(responseData.quantity);
			$("#viewprice").text(formatVND(responseData.price, ' VND'));
			$("#viewimage").text(responseData.image);
			$("#viewdescription").text(responseData.description);
			$("#viewdiscount").text(responseData.discount);
			$("#viewactive").text(responseData.active);
			$("#viewcreateDay").text(responseData.createDay);
			$("#viewupdateDate").text(responseData.updateDate);
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
}

function viewInvoice(id) {
	$("#modelView").modal('show');
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: invoicepageviewDetailApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData)
			$('#tbodyExample').empty();
			$.each(responseData, function(index) {
				console.log(responseData[index])
				$('#tbodyExample').append("<tr>\
										<td><img src='http://localhost:8080/uploads/"+ responseData[index].product.image +"'></td>\
										<td>"+ responseData[index].product.name + "</td>\
										<td>"+ responseData[index].product.category.name + "</td>\
										<td>"+ formatVND(responseData[index].price, ' VND') + "</td>\
										<td>"+ responseData[index].quantity + "</td>\
										<td>"+ formatVND(responseData[index].totail, ' VND') + "</td>\
									</tr>");
			})
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: invoicepageApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData)
			$("#invoiceCode").text(responseData.id);
			$("#fullName").text(responseData.user.fullName);
			$("#email").text(responseData.user.email);
			$("#address").text(responseData.deliveryAddress);
			//			switch ($(".status").val()){
			//				case 'delivered':
			//				
			//				break;
			//				case 'beingTransported':
			//				
			//				break;
			//				case 'waiting':
			//				
			//				break;
			//			}
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
}
