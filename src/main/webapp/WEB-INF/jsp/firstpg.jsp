<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=0">
<title>智能硬件</title>
<link rel="stylesheet" href="./css/weui.css" />
<link rel="stylesheet" href="./css/example.css" />
<!-- <script src="./js/zepto.min.js"></script> -->
<script src="./js/jquery-1.10.2.js"></script>
<!--css just for the preview-->
<link href="./css/styles.css" rel="stylesheet" type="text/css" />
<link href="./css/goal-thermometer.css" rel="stylesheet" type="text/css" />
<link class="include" rel="stylesheet" type="text/css"
	href="./css/jquery.jqplot.min.css" />
<script type="text/javascript">
	var currentAmount = 25;
</script>
<script type="text/javascript" src="./js/goal-thermometer.js"></script>
<script class="include" type="text/javascript"
	src="./js/jquery.jqplot.min.js"></script>
</head>
<body>
	<div id="wrapper">open id is : ${openid}</div>
	<div id="centered">
		<!--add this line to the html where you want the thermometer-->
		<div id="goal-thermometer"></div>
	</div>
	<div id="chart" style="height: 300px;"></div>
	<div class="bd">
		<div id="bottomToolbar" class="weui_grids">
			<a href="javascript:nearweek_zx()" class="weui_grid">
				<div class="weui_grid_icon">
					<i class="icon icon_cell"></i>
				</div>
				<p class="weui_grid_label" style="font-style: oblique;">近一周</p>
			</a> <a href="javascript:today_zx()" class="weui_grid">
				<div class="weui_grid_icon">
					<i class="icon icon_icons"></i>
				</div>
				<p class="weui_grid_label" style="font-style: oblique;">本日</p>
			</a> <a href="javascript:nearmonth_zx()" class="weui_grid">
				<div class="weui_grid_icon">
					<i class="icon icon_actionSheet"></i>
				</div>
				<p class="weui_grid_label" style="font-style: oblique;">近一月</p>
			</a>
		</div>
	</div>
	<script class="code" type="text/javascript">
		var lc = true;
		var plot_c1;
		function today_zx(){
			if(lc){
				$('#centered').hide();
				var data = [ [] ];
				var sineRenderer1 = function() {
					for (var i = 1; i <= 24; i += 2) {
						data[0].push([ i, Math.sin(i) ]);
					}
					return data;
				};
				if(plot_c1){
					var dd = [ [] ];
					var x = 0;
					for (var i = 1; i <= 24; i += 2) {
						dd[x]=[ i, Math.sin(i) ];
						x +=1;
					}
					plot_c1.series[0].data = dd;  
					plot_c1.axes.yaxis.reset();  
					plot_c1.axes.yaxis.ticks = '今日温度走势';  
					plot_c1.replot(); 
				}else{
					plot_c1 = $.jqplot('chart', [], {
						title : '今日温度走势',
						dataRenderer : sineRenderer1,
						axes : {
							xaxis : {
								pad : 1
							}
						}
					});
				}
				lc = false;
			}else{
				$('#chart').hide();
				$('#centered').show();
				lc = true;
			}
		}
		function nearweek_zx(){
			$('#centered').hide();
			var data = [ [] ];
			var sineRenderer1 = function() {
				for (var i = 1; i <= 7; i += 1) {
					data[0].push([ i, Math.sin(i) ]);
				}
				return data;
			};
			if(plot_c1){
				var dd = [ [] ];
				var x = 0;
				for (var i = 1; i <= 7; i += 1) {
					dd[x]=[ i, Math.sin(i) ];
					x +=1;
				}
				plot_c1.series[0].data = dd; 
				plot_c1.axes.yaxis.reset();  
				plot_c1.axes.yaxis.ticks = '近一周温度走势';  
				plot_c1.replot(); 
			}else{
				plot_c1 = $.jqplot('chart', [], {
					title : '近一周温度走势',
					dataRenderer : sineRenderer1,
					axes : {
						xaxis : {
							pad : 1
						}
					}
				});
			}
			lc = false;
		}
		function nearmonth_zx(){
			$('#centered').hide();
			var data = [ [] ];
			var sineRenderer1 = function() {
				for (var i = 1; i <= 31; i += 3) {
					data[0].push([ i, Math.sin(i) ]);
				}
				return data;
			};
			if(plot_c1){
				var dd = [ [] ];
				var x = 0;
				for (var i = 1; i <= 31; i += 3) {
					dd[x]=[ i, Math.sin(i) ];
					x +=1;
				}
				plot_c1.series[0].data = dd; 
				plot_c1.axes.yaxis.reset();  
				plot_c1.axes.yaxis.ticks = '近一月温度走势'; 
				plot_c1.replot(); 
			}else{
				plot_c1 = $.jqplot('chart', [], {
					title : '近一月温度走势',
					dataRenderer : sineRenderer1,
					axes : {
						xaxis : {
							pad : 1
						}
					}
				});
			}
			lc = false;
		}
	</script>
</body>
</html>