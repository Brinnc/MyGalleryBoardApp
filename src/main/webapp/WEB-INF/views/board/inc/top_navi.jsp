<%@ page contentType="text/html; charset=UTF-8"%>
<nav class="main-header navbar navbar-expand navbar-white navbar-light">
	<!-- Left navbar links -->
	<ul class="navbar-nav">

		<!--
		<li class="nav-item"><a class="nav-link" data-widget="pushmenu"
					href="#" role="button"><i class="fas fa-bars"></i></a></li>
		-->

		<li class="nav-item d-none d-sm-inline-block"><a href="#"
			onclick="loginForm()" class="nav-link">🧩Sign in</a></li>

		<li class="nav-item d-none d-sm-inline-block"><a href="#"
			onclick="registForm()" class="nav-link">🖍️Write</a></li>

	</ul>

	<!-- Right navbar links -->
	<ul class="navbar-nav ml-auto">
		<!-- Navbar Search -->
		<li class="nav-item"><a class="nav-link"
			data-widget="navbar-search" href="#" role="button"> <i
				class="fas fa-search"></i>
		</a>
			<div class="navbar-search-block">
				<form class="form-inline">
					<div class="input-group input-group-sm">
						<input class="form-control form-control-navbar" type="search"
							placeholder="Search" aria-label="Search">
						<div class="input-group-append">
							<button class="btn btn-navbar" type="submit">
								<i class="fas fa-search"></i>
							</button>
							<button class="btn btn-navbar" type="button"
								data-widget="navbar-search">
								<i class="fas fa-times"></i>
							</button>
						</div>
					</div>
				</form>
			</div></li>


		<li class="nav-item"><a class="nav-link" data-widget="fullscreen"
			href="#" role="button"> <i class="fas fa-expand-arrows-alt"></i>
		</a></li>

		<!-- 
		<li class="nav-item"><a class="nav-link"
			data-widget="control-sidebar" data-controlsidebar-slide="true"
			href="#" role="button"> <i class="fas fa-th-large"></i>
		</a></li>
		-->
	</ul>
</nav>

<script type="text/javascript">
	/* $(function(){
	 $("button").click(function(){
	 location.href="/board/registform";
	 });
	
	 }); */

	function loginForm() {
		location.href="/admin/loginform";
	}
	 
	function registForm() {
		location.href = "/board/registform";
	}
</script>