import React, { Component } from "react";
import BannerDataService from "../services/banner.service";
import { Link } from "react-router-dom";

export default class BannersList extends Component {
  constructor(props) {
    super(props);
    this.onChangeSearchTitle = this.onChangeSearchTitle.bind(this);
    this.retrieveTutorials = this.retrieveTutorials.bind(this);
    this.refreshList = this.refreshList.bind(this);
    this.setActiveTutorial = this.setActiveTutorial.bind(this);
    this.searchTitle = this.searchTitle.bind(this);

    this.state = {
      banners: [],
      currentBanner: null,
      currentIndex: -1,
      searchTitle: ""
    };
  }

  componentDidMount() {
    this.retrieveTutorials();
  }

  onChangeSearchTitle(e) {
    const searchTitle = e.target.value;

    this.setState({
      searchTitle: searchTitle
    });
  }

  retrieveTutorials() {
    BannerDataService.getAll()
      .then(response => {
        this.setState({
          banners: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  refreshList() {
    this.retrieveTutorials();
    this.setState({
      currentBanner: null,
      currentIndex: -1
    });
  }

  setActiveTutorial(banner, index) {
    this.setState({
      currentBanner: banner,
      currentIndex: index
    });
  }

  searchTitle() {
    this.setState({
      currentBanner: null,
      currentIndex: -1
    });

    BannerDataService.findByTitle(this.state.searchTitle)
      .then(response => {
        this.setState({
          banners: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    const { searchTitle, banners, currentBanner, currentIndex } = this.state;

    return (
      <div className="list row">
        <div className="col-md-8">
          <div className="input-group mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Search by title"
              value={searchTitle}
              onChange={this.onChangeSearchTitle}
            />
            <div className="input-group-append">
              <button
                className="btn btn-outline-secondary"
                type="button"
                onClick={this.searchTitle}
              >
                Search
              </button>
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <h4>Banners List</h4>

          <ul className="list-group">
            {banners &&
              banners.map((banner, index) => (
                <li
                  className={
                    "list-group-item " +
                    (index === currentIndex ? "active" : "")
                  }
                  onClick={() => this.setActiveTutorial(banner, index)}
                  key={index}
                >
                  <span  className={
                    "list-group-item " +
                    (index === currentIndex ? "active" : "")
                  }>{ banner.id}</span>
                  {banner.bannerName}

                </li>
              ))}
          </ul>

        </div>
        <div className="col-md-6">
          {currentBanner ? (
            <div>
              <h4>Banner</h4>
              <div>
                <label>
                  <strong>id:</strong>
                </label>{" "}
                {currentBanner.id}
              </div>
              <div>
                <label>
                  <strong>Name:</strong>
                </label>{" "}
                {currentBanner.bannerName}
              </div>
              <div>
                <label>
                  <strong>Content:</strong>
                </label>{" "}
                {currentBanner.content}
              </div>
              <div>
                <label>
                  <strong>Status:</strong>
                </label>{" "}
                {currentBanner.deleted ? "Deleted" : "Active" }
              </div>
              <div>
                <label>
                  <strong>Price:</strong>
                </label>{" "}
                {currentBanner.price }
              </div>

              <Link
                to={"/banners/" + currentBanner.id}
                className="btn btn-outline-secondary"
              >
                Edit
              </Link>
            </div>
          ) : (
            <div>
              <br />
              <p>Please click on a Banner...</p>
            </div>
          )}
        </div>
      </div>
    );
  }
}