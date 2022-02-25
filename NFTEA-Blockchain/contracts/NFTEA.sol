// contracts/MyNFT.sol
// SPDX-License-Identifier: MIT
//pragma experimental ABIEncoderV2;
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
//import "@openzeppelin/contracts/token/ERC721/extensions/ERC721Royalty.sol";
import "@openzeppelin/contracts/utils/Counters.sol";

contract NFTea is ERC721 { // ERC721Royalty to be added in US036
    struct Media {
        uint256 id;
        uint256 publisherId;
        address publisherAddress;
        string  mediaType;
        string  URL;
        string  permaLink;
        string  thumbnailURL;
        string  timestamp;
        string  publisherUsername;
        string  caption;
    }

    using Counters for Counters.Counter;
    Counters.Counter private _contentCount;
    mapping (uint256 => uint256) public nftToMedia;
    mapping (uint256 => Media) public contents;
    mapping (address => uint256) public contentCountByUser;

    constructor() ERC721("NF-TEA", "NFTEA") {}

    function mintNFTea (
        uint256 id,
        uint256 publisherId,
        string memory  mediaType,
        string memory  URL,
        string memory  permaLink,
        string memory  thumbnailURL,
        string memory  timestamp,
        string memory  publisherUsername,
        string memory  caption
    )
    public
    {
        require(nftToMedia[id] == 0, "An NFT with that content Id has already been minted.");

        _contentCount.increment();
        nftToMedia[id] = _contentCount.current();
        contentCountByUser[msg.sender] = contentCountByUser[msg.sender] + 1;

        contents[_contentCount.current()] = Media(
            id,
            publisherId,
            msg.sender,
            mediaType,
            URL,
            permaLink,
            thumbnailURL,
            timestamp,
            publisherUsername,
            caption
        );

        _mint(msg.sender, id);
    }

    function getMediaByUser () public view returns (Media[] memory) {
        require(contentCountByUser[msg.sender] != 0, "The user owns no NFTs.");

        Media[] memory memoryArray = new Media[](contentCountByUser[msg.sender]);
        uint256 j = 0;

        for(uint256 i = 1; i <= _contentCount.current() && j < contentCountByUser[msg.sender]; i++) {
            if(contents[i].publisherAddress == msg.sender) {
                memoryArray[j] = contents[i];
                j++;
            }
        }

        return memoryArray;
    }
}
