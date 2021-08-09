import React, { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AddBanner from "./components/add-banner.component";
import Banner from "./components/banner.component";
import BannersList from "./components/banners-list.component";

class App extends Component {
  render() {
    return (
      <div>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to={"/"} className="navbar-brand">
            BidService
          </Link>
          <div className="navbar-nav mr-auto">
            <li className="nav-item">
              <Link to={"/banners"} className="nav-link">
                Banners
              </Link>
            </li>
            <li className="nav-item">
              <Link to={"/banners/new"} className="nav-link">
                Add Banner
              </Link>
            </li>
          </div>
        </nav>

        <div className="container mt-3">
          <Switch>
            <Route exact path={["/", "/banners"]} component={BannersList} />
            <Route exact path="/banners/new" component={AddBanner} />
            <Route path="/banners/:id" component={Banner} />
          </Switch>
        </div>
      </div>
    );
  }
}

export default App;