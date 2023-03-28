let shopLikeApi = "http://localhost:8080/shop/my-favorites/like"
let shopDisLikeApi = "http://localhost:8080/shop/my-favorites/disLike"
let shopMyOrderApi = "http://localhost:8080/shop/my-order/add_to_cart"

function formatVND(n, currency) {
  return n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,') + currency;
}

function viewLike(id) {
	$("#baseFavitories").modal('show');
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: shopLikeApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData);
			$("#viewimage").attr('src', '/uploads/' + responseData.image);
			$("#viewname").text(responseData.name);
			if (responseData.discount == 0) {
				$("#viewprice").text(formatVND(responseData.price, ' VND'));
			} else {
				$("#viewprice").text(formatVND(responseData.price * responseData.discount * 0.01, ' VND'));
			}
			$("#viewdescription").text(responseData.description);
			$('#yesOption').attr('href', '/shop/my-favorites/liked?id=' + responseData.id);
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
}

function viewDisLike(id) {
	$("#baseDisLike").modal('show');
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: shopDisLikeApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData);
			$("#viewimage").attr('src', '/uploads/' + responseData.image);
			$("#viewname").text(responseData.name);
			if (responseData.discount == 0) {
				$("#viewprice").text(formatVND(responseData.price, ' VND'));
			} else {
				$("#viewprice").text(formatVND(responseData.price * responseData.discount * 0.01, ' VND'));
			}
			$("#viewdescription").text(responseData.description);
			$('#yesOption').attr('href', '/shop/my-favorites/disLiked?id=' + responseData.id);
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
}

function viewAddToCart(id) {
	$("#baseAddToCart").modal('show');
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: shopMyOrderApi + "/" + id,
		data: JSON.stringify(id),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(responseData) {
			console.log(responseData);
			$("#viewimageCart").attr('src', '/uploads/' + responseData.image);
			$("#viewnameCart").text(responseData.name);
			if (responseData.discount == 0) {
				$("#viewpriceCart").text(formatVND(responseData.price, ' VND'));
			} else {
				$("#viewpriceCart").text(formatVND(responseData.price * responseData.discount * 0.01, ' VND'));
			}
			$('#yesOptionCart').attr('href', '/shop/my-order/add?id=' + responseData.id);
		},
		error: function(e) {
			console.log("ERROR : ", e);
		}
	});
}