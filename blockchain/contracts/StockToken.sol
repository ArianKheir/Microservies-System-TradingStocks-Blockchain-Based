// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract StockToken is ERC20, Ownable {
    string private _stockSymbol;
    
    constructor(string memory name, string memory symbol) 
        ERC20(name, symbol) Ownable(msg.sender) {
        _stockSymbol = symbol;
    }
    
    function mint(address to, uint256 amount) external onlyOwner {
        _mint(to, amount);
    }
    
    function burn(address from, uint256 amount) external onlyOwner {
        _burn(from, amount);
    }
    
    function getStockSymbol() external view returns (string memory) {
        return _stockSymbol;
    }
}

