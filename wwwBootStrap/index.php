<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width,inital-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <!-- Optional Bootstrap theme -->
    <title>Masjid Umar</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    <link href='https://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>
</script>
<style type="text/css">
	.row > div{
        margin-bottom: 15px;
    }
	.header{
        min-height: 90px;
    }
    .footer{
        min-height: 60px;
    }
    .header, .footer{
        background: #2f2f2f;
    }
    .sidebar{
        background: #dbdfe5;
    }
    .content{
        background: #b4bac0;
    }
    .sidebar, .content{
        min-height: 300px;
    }
    
</style>
</head>
<body>
    <?php include "navigation.php"; ?>     
         <!-- Carousel
    ================================================== -->
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
            <!-- Indicators -->
            <ol class="carousel-indicators">
              <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
              <li data-target="#myCarousel" data-slide-to="1"></li>
              <li data-target="#myCarousel" data-slide-to="2"></li>
            </ol>
            <div class="carousel-inner" role="listbox">
              <div class="item active">
                <img class="img-responsive center-block" class="first-slide" src="img/sampleimage.jpg" First slide>
                <div class="container">
                  
                </div>
              </div>
              <div class="item">
                <img class="img-responsive center-block" class="second-slide" src="img/sampleimage.jpg" alt="Second slide">
                <div class="container">
                </div>
              </div>
              <div class="item">
                <img class="img-responsive center-block" class="third-slide" src="img/sampleimage.jpg" alt="Third slide">
                <div class="container">
                  =
                </div>
              </div>
            </div>
            <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
              <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
              <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
              <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
              <span class="sr-only">Next</span>
            </a>
          </div><!-- /.carousel -->

      
    <!-- START THE FEATURETTES -->
    <div class = "container marketing">
    <hr class="featurette-divider">

    <div class="row featurette">
    <div class="col-md-7">
        <h2 class="featurette-heading">Our Mission.<span class="text-muted"></span></h2>
        <p class="lead">Relying on the Qur'an and Sunnah to create a model Islamic Center offering
            regular prayer services and learning activities for all ages while operating
            as a local leader in Interfaith and Outreach activities.
        </p>
    </div>
    <div class="col-md-5">
        <img class="featurette-image img-responsive center-block" src= "img/sampleimage.jpg" alt="Generic placeholder image">
    </div>
    </div>

    <hr class="featurette-divider">

    <div class="row featurette">
    <div class="col-md-7 col-md-push-5">
        <h2 class="featurette-heading">Daily Classes <span class="text-muted">for all age groups.</span></h2>
        <p class="lead">Join classes every day between Maghrib and Isha for all age groups. Classes in 
            Qur'anic recitation, Seerah and basic tenets of our faith are all found here. </p>
    </div>
    <div class="col-md-5 col-md-pull-7">
        <img class="featurette-image img-responsive center-block" src="img/sampleimage.jpg" alt="Generic placeholder image">
    </div>
    </div>

    <hr class="featurette-divider">

    <div class="row featurette">
    <div class="col-md-7">
        <h2 class="featurette-heading">Umrah and Hajj.<span class="text-muted"></span></h2>
        <p class="lead">Every year, members of our community travel for Hajj and Umrah. We offer specialized classes 
            describing rituals and helping our community members prepare for the most important journey
            of their lifetimes. 
        </p>
    </div>
    <div class="col-md-5">
        <img class="featurette-image img-responsive center-block" src="img/sampleimage.jpg" alt="Generic placeholder image">
    </div>
    </div>

    <hr class="featurette-divider">

    <!-- /END THE FEATURETTES -->
    <?php include "footer.php"; ?>
    </div><!-- /.container -->
	<!-- Open the output in a new blank tab (Click the arrow next to "Show Output" button) and resize the browser window to understand how the Bootstrap responsive grid system works. -->
</body>
</html>


